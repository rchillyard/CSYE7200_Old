/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200

object P00 {
  def flatten[X](xss: Seq[Seq[X]]): Seq[X] = {
    // TO BE IMPLEMENTED
    ???
  }

  def fill[X](n: Int)(x: X): Seq[X] = {
    // TO BE IMPLEMENTED
    ???
  }
}

object P01 {

  def last[X](xs: List[X]): X = ??? // TO BE IMPLEMENTED
}

object P02 {

  def penultimate[X](xs: List[X]): X = ??? // TO BE IMPLEMENTED
}

object P03 {

  def kth[X](k: Int, xs: List[X]): X = ??? // TO BE IMPLEMENTED
}

object P04 {

  def length[X](xs: List[X]): Int = {
    // TO BE IMPLEMENTED
    ???
  }
}

object P05 {

  def reverse[X](xs: List[X]): List[X] = {
    // TO BE IMPLEMENTED
    ???
  }
}

object P06 {

  // inefficient solution
  def isPalindrome[X](xs: List[X]): Boolean = ??? // TO BE IMPLEMENTED
}

object P07 {

  type Sequence = Seq[Any]

  def flatten(xs: Sequence): Sequence = {
    // TO BE IMPLEMENTED
    ???
  }
}

object P08 {

  def compress[X](xs: List[X]): Seq[X] = {
    // TO BE IMPLEMENTED
    ???
  }
}

object P09 {

  def pack[X](xs: List[X]): Seq[Seq[X]] = {
    // TO BE IMPLEMENTED
    ???
  }
}

object P10 {

  def encode[X](xs: List[X]): Seq[(Int, X)] = ??? // TO BE IMPLEMENTED
}

object P11 {

  def encodeModified[X](xs: List[X]): Seq[Any] = ??? // TO BE IMPLEMENTED
}

object P12 {

  def decode[X](xIs: Seq[(Int, X)]): Seq[X] = ??? // TO BE IMPLEMENTED
}

object P13 {

  def encodeDirect[X](xs: List[X]): Seq[(Int, X)] = {
    // TO BE IMPLEMENTED
    ???
  }
}

object P14 {

  def duplicate[X](xs: Seq[X]): Seq[X] = {
    // TO BE IMPLEMENTED
    ???
  }
}

object P15 {

  def duplicateN[X](n: Int, xs: Seq[X]): Seq[X] = {
    // TO BE IMPLEMENTED
    ???
  }
}
