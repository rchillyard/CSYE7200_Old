/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200

object P01 {

  def last(xs: List[Int]): Int = xs match {
    case Nil => throw new Exception("no such element")
    case x :: Nil => x
    case x :: t => last(t)
  }
}

object P02 {

  def penultimate(xs: List[Int]): Int = ??? // TO BE IMPLEMENTED
}

object P03 {

  def kth(k: Int, xs: List[Int]): Int = ??? // TO BE IMPLEMENTED
}

object P04 {

  def length(xs: List[Int]): Int = ??? // TO BE IMPLEMENTED


}
