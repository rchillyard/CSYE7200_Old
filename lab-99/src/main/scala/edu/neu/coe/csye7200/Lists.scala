/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200

object P01 {

  def last(xs: List[Int]): Int = xs match {
    case x :: Nil => x
    case x :: t => last(t)
    case _ => throw new NoSuchElementException
  }
}

object P02 {

  def penultimate(xs: List[Int]): Int = xs match {
    case x :: _ :: Nil => x
    case x :: t => penultimate(t)
    case _ => throw new NoSuchElementException
  }
}

object P03 {

  def kth(k: Int, xs: List[Int]): Int = (k, xs) match {
    case (0, x :: _) => x
    case (n, x :: t) => kth(n-1, t)
    case (_, _) => throw new NoSuchElementException
  }
}

object P04 {

  def length(xs: List[Int]): Int = {
    def inner(r: Int, _xs: List[Int]) =
    _xs match {
      case Nil => 0
      case h :: t => 1+length(t)
    }
    inner(0, xs)
  }
}