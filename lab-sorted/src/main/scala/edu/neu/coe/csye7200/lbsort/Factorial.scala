package edu.neu.coe.csye7200.lbsort

import scala.annotation.tailrec

object Factorial {

  def factorial(n: Int): Long = {
    @tailrec
    def inner(r: Long, i: Int): Long = i match {
      case 0 => r
      case _ => inner(r * i, i - 1)
    }
    inner(1, n)
  }

}
