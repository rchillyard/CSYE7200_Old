package edu.neu.coe.csye7200

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

/**
  * Created by scalaprof on 2/17/17.
  */
object FutureExercise extends App {

  val chunk = 10000 // Try it first with chunk = 10000 and build up to 1000000
  def integers(i: Int, n: Int): Stream[Int] = Stream.from(i) take n

  def sum[N: Numeric](is: Stream[N]): BigInt = is.foldLeft[BigInt](0)(_ + implicitly[Numeric[N]].toLong(_))

  def asyncSum(is: Stream[Int]): Future[BigInt] = Future {
    val x = sum(is)
    System.err.println(s"${is.head} is done with sum $x")
    x
  }

  val xfs = for (i <- 0 to 9) yield asyncSum(integers(i * chunk + 1, chunk))
  val xsf = Future.sequence(xfs)
  val xf = for (ls <- xsf) yield sum(ls.toStream)
  xf foreach { x => println(s"Sum: $x") }
  private val expected = xf filter (_ == BigInt(chunk * 10 * (chunk * 10 + 1L) / 2))
  expected foreach { x => println("OK") }

  Await.ready(expected, 10000 milli)
  println("Goodbye")
}
