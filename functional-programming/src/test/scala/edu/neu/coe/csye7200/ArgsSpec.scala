/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200

import org.scalatest.{FlatSpec, Matchers}

import scala.util.Success

class ArgsSpec extends FlatSpec with Matchers{

  def printFilename(s: String) = println(s"filename: $s")
  val processor = Map[String,Option[String=>Unit]]("f"-> Some(printFilename))

  behavior of "SimpleArgParser"

  it should "parse -f" in {
    val p = new SimpleArgParser
    p.parseToken("-f") should matchPattern { case Success(p.Command("f")) => }
  }
  it should "parse filename" in {
    val p = new SimpleArgParser
    p.parseToken("filename") should matchPattern { case Success(p.Argument("filename")) => }
  }

  behavior of "Args"

  it should "parse -f filename" in {
    val args = Array("-f", "filename")
    val as = Args(args)
    as.xas.length shouldBe 1
    as.xas.head shouldBe Arg(Some("f"),Some("filename"))
  }

  behavior of "Arg"

//  it should "parse -f filename" in {
//    val args = Array("-f", "filename")
//    val as = Args(args)
//    println(s"as: $as")
//  }

  //  behavior of "posixArg"
  //
  //  it should "parse -f filename" in {
  //    val p = new PosixArgParser
  //    val as: p.ParseResult[List[Arg[String]]] = p.parseAll(p.posixArgs,"-f filename")
  //    println(s"as: $as")
  //  }

}
