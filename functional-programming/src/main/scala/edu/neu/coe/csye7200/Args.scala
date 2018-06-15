/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200

import scala.util._
import scala.util.parsing.combinator.JavaTokenParsers

case class Arg[X](name: Option[String], value: Option[X]) {

  def byName(w: String): Option[Arg[X]] = name match {
    case Some(`w`) => Some(this)
    case _ => None
  }

  def map[Y](f: X => Y): Arg[Y] = Arg(name, value map f)

  def asMaybeTuple: Option[(String, X)] = name match {
    case Some(w) => value match {
      case Some(x) => Some((w, x))
      case _ => None
    }
    case _ => None
  }

  def toY[Y: Derivable]: Y = value match {
    case Some(x) => implicitly[Derivable[Y]].deriveFrom(x)
    case _ => throw NoValueException(name)
  }

  def process(fm: Map[String, Option[X] => Unit]): Try[Option[X]] = {
    def processFuncMaybe(fo: Option[Option[X] => Unit]): Try[Option[X]] = fo match {
      case Some(f) => Try(f(value)).map(_ => None)
      case None => Failure(AnonymousNotFoundException)
    }

    def process(c: String): Try[Option[X]] = processFuncMaybe(fm.get(c)).recoverWith({ case AnonymousNotFoundException => Failure(NotFoundException(c)) })

    name match {
      case Some(c) => process(c)
      case None => Success(value)
    }
  }

  override def toString: String = s"Arg: command ${name.getOrElse("anonymous")} with value: ${value.getOrElse("none")}"
}

object Arg {
  def apply(c: String): Arg[String] = Arg(Some(c), None)

  def apply(c: String, a: String): Arg[String] = Arg(Some(c), Some(a))
}

case class Args[X](xas: Seq[Arg[X]]) extends Traversable[Arg[X]] {
  def map[Y](f: X => Y): Args[Y] = Args(for (xa <- xas) yield xa.map(f))

  def extract: Map[String, X] = (for (xa <- xas) yield xa.asMaybeTuple).flatten.toMap

  /**
    * Method to get an Arg whose name matches the given string.
    *
    * @param w the string to match
    * @return Some(arg) if the name matches, else None
    */
  def getArg(w: String): Option[Arg[X]] = {
    MonadOps.sequence(for (xa <- xas) yield xa.byName(w)) match {
      case Some(xas_) => if (xas_.size == 1) Some(xas_.head) else throw AmbiguousNameException(w)
      case _ => None
    }
  }

  def process(fm: Map[String, Option[X] => Unit]): Try[Seq[X]] =
    MonadOps.sequence(for (xa <- xas) yield for (x <- xa.process(fm)) yield x) match {
      case Success(xos) => Success(xos.flatten)
      case Failure(x) => Failure(x)
    }

  def iterator: Iterator[Arg[X]] = xas.iterator

  def foreach[U](f: Arg[X] => U): Unit = xas foreach f

}

object Args {
  def create(args: Arg[String]*): Args[String] = apply(args)

  def parse(args: Array[String]): Args[String] = {
    val p = new SimpleArgParser

    def inner(r: Seq[Arg[String]], w: Seq[p.Token]): Seq[Arg[String]] = w match {
      case Nil => r
      case p.Command(c) :: p.Argument(a) :: t => inner(r :+ Arg(c, a), t)
      case p.Command(c) :: t => inner(r :+ Arg(c), t)
      case p.Argument(a) :: t => inner(r :+ Arg(None, Some(a)), t)
    }

    val tys = for (a <- args) yield p.parseToken(a)
    val ts = MonadOps.sequence(tys) match {
      case Success(ts_) => ts_
      case Failure(x) => System.err.println(x.getLocalizedMessage); Seq[p.Token]()
    }
    Args(inner(Seq(), ts))
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

/**
  * Type-class trait to allow conversion from type X to type T
  *
  * @tparam T the result type
  */
trait Derivable[T] {
  /**
    * Method to convert an X to a T
    *
    * @param x the X value
    * @tparam X the input type
    * @return a T
    */
  def deriveFrom[X](x: X): T
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

  def token: Parser[Token] = command | argument

  def command: Parser[Command] = "-" ~> cmdR ^^ (s => Command(s))

  def argument: Parser[Argument] = argR ^^ (s => Argument(s))

  private val cmdR = """[a-z]+""".r
  private val argR = """\w+""".r
}

class PosixArgParser extends JavaTokenParsers {

  def posixCommandLine: Parser[Args[String]] = rep(posixArgs) ^^ (as => Args(as.flatten))

  def posixArgs: Parser[List[Arg[String]]] = cmdR ~ rep(cmdR) ~ opt(argR) ^^ { case c ~ cs ~ a => (c :: cs.init).map(toCmd) :+ Arg(Some(cs.last), a) }

  def toCmd(w: String): Arg[String] = Arg(Some(w), None)

  private val cmdR = """-?([a-z])""".r
  private val argR = """\s(\w+)""".r
}

abstract class ArgsException(s: String) extends Exception(s"Args exception: $s")

case object AnonymousNotFoundException extends ArgsException("no anonymous arg found")

case class NotFoundException(command: String) extends ArgsException(s"Arg: command $command not found")

case class AmbiguousNameException(name: String) extends ArgsException(s"$name ambiguous")

case class ParseException(cause: String) extends ArgsException(cause)

case class NoValueException(name: Option[String]) extends ArgsException(s"Arg: command ${name.getOrElse("anonymous")} has no value")