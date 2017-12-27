package edu.neu.coe.csye7200.parse

import scala.util.parsing.combinator.JavaTokenParsers

/**
  * Created by scalaprof on 12/1/16.
  */
class BadParser extends JavaTokenParsers {
  def name = ident ~ opt(ident) ~ ident ^^ { case f ~ mo ~ l => (f,mo,l)}
}

object BadParser extends App {
  val p = new BadParser
  val r = p.parseAll(p.name,"Martin Scala Odersky")
  val first = r match {
    case p.Success((f,m,l),_) => f
  }
  println(first)
}