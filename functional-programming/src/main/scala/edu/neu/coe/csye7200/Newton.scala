package edu.neu.coe.csye7200

import scala.annotation.tailrec
import scala.util._

/**
  * Created by scalaprof on 1/20/17.
  */
case class Newton(w: String, f: Double => Double, dfbydx: Double => Double) {

  override def toString: String = w

  private def step(xy: Try[Double], yy: Try[Double]) = for (x <- xy; y <- yy) yield x - y / dfbydx(x)

  def solve(tries: Int, threshold: Double, initial: Double): Try[Double] = {
    @tailrec def inner(ry: Try[Double], n: Int): Try[Double] = {
      val yy = for (r <- ry) yield f(r)
      (for (y <- yy) yield math.abs(y) < threshold) match {
        case Success(true) => ry
        case _ =>
          if (n == 0) Failure(new Exception(s"failed to converge in $tries tries, starting from x=$initial and where threshold=$threshold"))
          else inner(step(ry, yy), n - 1)
      }
    }

    inner(Success(initial), tries)
  }

}

object Newton extends App {
  val newton = Newton("cos(x)-x", x => math.cos(x) - x, x => -math.sin(x) - 1)
  newton.solve(10, 1E-10, 1) match {
    case Success(x) => println(s"""The solution to "$newton=0" is $x""")
    case Failure(t) => System.err.println(s"""$newton unsuccessful: ${t.getLocalizedMessage}""")
  }
}