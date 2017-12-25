package edu.neu.coe.csye7200.factorial

trait Ring[X] {
  def plus(x1: X, x2: X): X
  def times(x1: X, x2: X): X
}

object DoubleRing {
  trait DoubleRing extends Ring[Double] {
    def plus(x1: Double, x2: Double): Double = x1+x2
    def times(x1: Double, x2: Double): Double = x1*x2
  }
  implicit object DoubleRing extends DoubleRing
}
/**
  * Created by scalaprof on 10/28/16.
  */
object SumOfSquares extends App {
  def sumOfSquares[X : Numeric](xs: Seq[X]) = {
    val n = implicitly[Numeric[X]]
    xs map (x => n.times(x, x)) sum
  }

  println(sumOfSquares(Seq(1,2,3,4,5)))
}