/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200.cache

import scala.concurrent.Future
import scala.util._

case class Portfolio(positions: Seq[Position]) {

  def value(cache: Cache[String, Double]): Future[Double] = ??? // TODO

}

case class Position(symbol: String, quantity: Double) {
  def value(cache: Cache[String, Double]): Future[Double] = ??? // TODO
}

object Portfolio {
  private def sequence[X](xys: Seq[Try[X]]): Try[Seq[X]] = (Try(Seq[X]()) /: xys) {
    (xsy, xy) => for (xs <- xsy; x <- xy) yield xs :+ x
  }

  def parse(ws: Seq[String]): Try[Portfolio] = sequence(ws map Position.parse) map Portfolio.apply
}

object Position {
  val positionR = """(\w+)\s+(\d+(\.\d+))""".r
  def parse(w: String): Try[Position] = w match {
    case positionR(a, b, _) => Try(Position(a,b.toDouble))
    case _ => Failure(new Exception(s"cannot parse $w as a Position"))
  }

  def value(cache: Cache[String, Double])(w: String): Future[Double] = ??? // TODO

  private def flatten[X](xfy: Try[Future[X]]): Future[X] =
    xfy match {
      case Success(xf) => xf
      case Failure(e) => Future.failed(e)
    }

}
