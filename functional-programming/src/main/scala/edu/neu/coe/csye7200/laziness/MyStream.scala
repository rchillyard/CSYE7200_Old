/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200.laziness

trait MyStream[X] {
  val head: X
  def tail(): MyStream[X]
  def take(n: Int): Seq[X] = n match {
    case 0 => Nil
    case 1 => Seq(head)
    case _ => take(1) ++ tail().take(n-1)
  }
  override def toString = s"$head, ???"
}

case class Cons[X](head: X, lazyTail: ()=>MyStream[X]) extends MyStream[X] {
  def tail() = lazyTail()
}

case object EmptyStream extends MyStream[Nothing] {
  val head = throw new Exception("empty stream")
  def tail() = EmptyStream
}

