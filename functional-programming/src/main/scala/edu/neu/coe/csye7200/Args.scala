/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200

import scala.util._
import scala.util.parsing.combinator.JavaTokenParsers

case class Arg[X](name: Option[String], value: Option[X]) {
  def map[Y](f: X => Y): Arg[Y] = Arg(name, value map f)

  def process(fm: Map[String, Option[X] => Unit]): Try[Option[X]] = {
    def processFunc(fo: Option[Option[X] => Unit]): Try[Option[X]] = fo match {
      case Some(f) => val xoy = Try(f(value)); xoy.map(_ => None)
      case None => Failure(AnonymousNotFoundException)
    }

    def process(c: String): Try[Option[X]] = processFunc(fm.get(c)).recoverWith({ case AnonymousNotFoundException => Failure(NotFoundException(c)) })

    name match {
      case Some(c) => process(c)
      case None => Success(value)
    }
  }
}

object Arg {
  def apply(c: String): Arg[String] = Arg(Some(c), None)

  def apply(c: String, a: String): Arg[String] = Arg(Some(c), Some(a))
}

case class Args[X](xas: Seq[Arg[X]]) {
  def map[Y](f: X => Y): Args[Y] = Args(for (xa <- xas) yield xa.map(f))

  def process(fm: Map[String, Option[X] => Unit]): Try[Seq[X]] =
    MonadOps.sequence(for (xa <- xas) yield for (x <- xa.process(fm)) yield x) match {
      case Success(xos) => Success(xos.flatten)
      case Failure(x) => Failure(x)
    }
}

class SimpleArgParser extends JavaTokenParsers {
  def parseToken(s: String): Try[Token] = parseAll(token, s) match {
    case Success(t, _) => scala.util.Success(t)
    case _ => scala.util.Failure(new Exception(s"could not parse '$s' as a token"))
  }

  trait Token {
    def s: String
  }

  case class Command(s: String) extends Token

  case class Argument(s: String) extends Token

  case object NotAToken extends Token {
    def s: String = null
  }

  def token: Parser[Token] = command | argument

  def command: Parser[Command] = "-" ~> cmdR ^^ (s => Command(s))

  def argument: Parser[Argument] = argR ^^ (s => Argument(s))

  def toCmd(w: String): Arg[String] = Arg(Some(w), None)

  val cmdR = """[a-z]+""".r
  val argR = """\w+""".r
}

class PosixArgParser extends JavaTokenParsers {

  def posixCommandLine: Parser[Args[String]] = rep(posixArgs) ^^ { case as => Args(as.flatten) }

  def posixArgs: Parser[List[Arg[String]]] = cmdR ~ rep(cmdR) ~ opt(argR) ^^ { case c ~ cs ~ a => (c :: cs.init).map(toCmd) :+ Arg(Some(cs.last), a) }

  def toCmd(w: String): Arg[String] = Arg(Some(w), None)

  val cmdR = """-?([a-z])""".r
  val argR = """\s(\w+)""".r
}

object Args {
  val p = new SimpleArgParser

  def apply(args: Array[String]): Args[String] = {
    val tys = for (a <- args) yield p.parseToken(a)
    val tsy: Try[Seq[p.Token]] = MonadOps.sequence(tys)
    val ts: Seq[p.Token] = tsy match {
      case Success(ts) => ts
      case Failure(x) => System.err.println(x.getLocalizedMessage); Seq[p.Token]()
    }

    // CONSIDER implementing this by having a variable index into the ts sequence thus eliminating lastToken and also using Arg



    // CONSIDER implementing this as a finite state machine
    var lastToken: p.Token = p.NotAToken

    def processToken(i: Int): Option[Arg[String]] = ts(i) match {
      case t2@p.Command(s2) => lastToken match {
        case t1@p.Command(s1) => lastToken = t2; Some(Arg(s1))
        case t1@p.Argument(s1) => lastToken = t2; Some(Arg(None, Some(s1)))
        case p.NotAToken => lastToken = t2; None
      }
      case t2@p.Argument(s2) => lastToken match {
        case t1@p.Command(s1) => lastToken = p.NotAToken; Some(Arg(s1, s2))
        case t1@p.Argument(s1) => lastToken = t2; Some(Arg(None, Some(s1)))
        case p.NotAToken => lastToken = t2; None
      }
    }

    var argList = List[Option[Arg[String]]]()
    for (i <- ts.indices) argList = argList :+ processToken(i)
    Args(argList.flatten)
  }

  //  def apply(args: Array[String]): Args[String] = {
  //    parsePosixCommandLine(args.mkString(" ")) match {
  //      case Success(sa) => sa
  //      case Failure(x) => throw x
  //    }
  //  }
  //
  //  def parsePosixCommandLine(w: String): Try[Args[String]] = {
  //    val p = new PosixArgParser
  //    p.parseAll(p.posixCommandLine, w) match {
  //      case p.Success(sa: Args[String], _) => Success(sa)
  //      case p.Failure(e, _) => Failure(ParseException(e))
  //      case p.Error(e, _) => Failure(ParseException(e))
  //      case _ => Failure(new Exception("logic error"))
  //    }

}

case object AnonymousNotFoundException extends Exception()

case class NotFoundException(command: String) extends Exception(s"Arg: command $command not found")

case class ParseException(cause: String) extends Exception(cause)