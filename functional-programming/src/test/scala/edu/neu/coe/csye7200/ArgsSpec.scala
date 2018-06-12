/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200

import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

class ArgsSpec extends FlatSpec with Matchers{

  private val argFilename = "argFilename"
  private val nameF = "f"
  private val cmdF = "-" + nameF
  private val sX = "x"
  private val sY = "y"
  private val s1 = "1"
  private val x1 = 1

  private def printFilename(so: Option[String]): Unit = so.foreach(s => println(s"$argFilename: $s"))

  val processor: Map[String, Option[String] => Unit] = Map[String, Option[String] => Unit](nameF -> printFilename)

  behavior of "Arg"

  it should "work for " + sX + ": " + s1 in {
    val target = Arg(sX, s1)
    target.name shouldBe Some(sX)
    target.value shouldBe Some(s1)
  }

  it should "implement map" in {
    val target = Arg(sX, s1)
    val result = target.map(_.toInt)
    result.value shouldBe Some(x1)
  }

  it should "implement map with exception" in {
    val target = Arg(sX, sX)
    a[java.lang.NumberFormatException] shouldBe thrownBy(target.map(_.toInt))
  }

  it should "process " + sX + ": append" in {
    val sb = new StringBuilder
    val processor = Map[String, Option[String] => Unit](sX ->[Option[String] => Unit] { x => sb.append(x) })
    val target = Arg(sX, s1)
    val result = target.process(processor)
    result should matchPattern { case Success(_) => }
    sb.toString shouldBe "Some(" + s1 + ")"
  }

  it should "not process " + sY + ": append" in {
    val sb = new StringBuilder
    val processor = Map[String, Option[String] => Unit](sX ->[Option[String] => Unit] { x => sb.append(x) })
    val target = Arg(sY, s1)
    val result = target.process(processor)
    result should matchPattern { case Failure(_) => }
  }

  behavior of "Args"

  it should "work" in {
    val target = Args.create(Arg(sX, s1))
    target.size shouldBe 1
    target.head.name shouldBe Some(sX)
    target.head.value shouldBe Some(s1)
  }

  it should "implement map" in {
    val target = Args.create(Arg(sX, s1))
    val result = target.map[Int](_.toInt)
    result.head.value shouldBe Some(x1)
  }

  it should "process " + sX + ": append" in {
    val sA = "a"
    val sb = new StringBuilder
    val processor = Map[String, Option[String] => Unit](sX ->[Option[String] => Unit] { case Some(x) => sb.append(x); case _ => })
    val target = Args.create(Arg(sX, s1), Arg(sX, sA))
    val result = target.process(processor)
    result should matchPattern { case Success(_) => }
    sb.toString shouldBe s1 + sA
  }

  it should "parse " + cmdF + " " + argFilename in {
    val args = Array(cmdF, argFilename)
    val as = Args.parse(args)
    as.xas.length shouldBe 1
    as.xas.head shouldBe Arg(Some(nameF), Some(argFilename))
  }

  it should "parse 1 2 3" in {
    val sa = Args.parse(Array("1", "2", "3"))
    sa.xas.length shouldBe 3
    sa.xas.head shouldBe Arg(None, Some("1"))
    val xa = sa.map[Int](_.toInt)
    xa shouldBe Args(Seq(Arg(None, Some(1)), Arg(None, Some(2)), Arg(None, Some(3))))
    val processor = Map[String, Option[Int] => Unit]()
    xa.process(processor) should matchPattern { case Success(Seq(1, 2, 3)) => }
  }

  behavior of "SimpleArgParser"

  val p = new SimpleArgParser

  it should "parse command " + cmdF in {
    p.parseAll(p.command,cmdF) should matchPattern { case p.Success(p.Command(`nameF`), _) => }
  }

  it should "not parse command -X" in {
    p.parseAll(p.command,"-X") should matchPattern { case p.Failure(_, _) => }
  }

  it should "parse argument " + argFilename in {
    p.parseAll(p.argument,argFilename) should matchPattern { case p.Success(p.Argument(`argFilename`), _) => }
  }

  it should "not parse argument -x" in {
    p.parseAll(p.argument,"-x") should matchPattern { case p.Failure(_, _) => }
  }

  it should "parse token " + cmdF in {
    p.parseAll(p.token,cmdF) should matchPattern { case p.Success(p.Command(`nameF`), _) => }
  }

  it should "parse token " + argFilename in {
    p.parseAll(p.token,argFilename) should matchPattern { case p.Success(p.Argument(`argFilename`), _) => }
  }

  it should "parse " + cmdF in {
    p.parseToken(cmdF) should matchPattern { case Success(p.Command(`nameF`)) => }
  }

  it should "parse " + argFilename in {
    p.parseToken(argFilename) should matchPattern { case Success(p.Argument(`argFilename`)) => }
  }

  //  behavior of "posixArg"
  //
  //  it should "parse -f argFilename" in {
  //    val p = new PosixArgParser
  //    val as: p.ParseResult[List[Arg[String]]] = p.parseAll(p.posixArgs,"-f argFilename")
  //    println(s"as: $as")
  //  }

}
