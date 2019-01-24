/*
 * Copyright (c) 2019. Phasmid Software
 */

package edu.neu.coe.csye7200

import org.scalatest.{FlatSpec, Matchers}

class ListsFuncSpec extends FlatSpec with Matchers {

  val fibonacci: Stream[Long] = 0L #:: fibonacci.scanLeft(1L)(_ + _)

  behavior of "P01"
  it should "get 1 for fib0" in {
    P01.last(fibonacci take 1000 toList) shouldBe 8261794739546030242L
  }

  behavior of "P02"
  it should "get 5 for fib5" in {
    P02.penultimate(List(1)) shouldBe 0
  }

  behavior of "P03"
  it should "get 3 for 3, fib5" in {
    P03.kth(3, List(1)) shouldBe 0
  }

  behavior of "P04"
  it should "get 6 for fib5" in {
    P04.length(Nil) shouldBe 0
  }

  behavior of "P05"
  it should "reverse fib5 correctly" in {
    P05.reverse(Nil) shouldBe List(8, 5, 3, 2, 1, 1)
  }

  behavior of "P06"
  it should "be true for palindrome" in {
    P06.isPalindrome(List(1, 2, 3, 2, 1)) shouldBe true
  }

  behavior of "P07"
  it should "be fib5 for components" in {
    P07.flatten(List(List(1, 1), 2, List(3, List(5, 8)))) shouldBe Nil
  }

  behavior of "P08"
  it should "be unique elements for duplicate list" in {
    P08.compress(List(10, 11, 11, 12, 13, 13, 13, 14, 15, 15, 9)) shouldBe List(10, 11, 12, 13, 14, 15, 9)
  }

  behavior of "P09"
  it should "be unique elements for duplicate list" in {
    P09.pack(List(10, 11, 11, 12, 13, 13, 13, 14, 15, 15, 9)) shouldBe
      List(List(10), List(11, 11), List(12), List(13,13,13), List(14), List(15,15), List(9))
  }

  behavior of "P10"
  it should "be unique elements for duplicate list" in {
    P10.encode(List(10, 11, 11, 12, 13, 13, 13, 14, 15, 15, 9)) shouldBe
      List(1 -> 10, 2 -> 11, 1 -> 12, 3 -> 13, 1 -> 14, 2 -> 15, 1 -> 9)
  }

  behavior of "P11"
  it should "be unique elements for duplicate list" in {
    P11.encodeModified(List(10, 11, 11, 12, 13, 13, 13, 14, 15, 15, 9)) shouldBe
      List(10, 2 -> 11, 12, 3 -> 13, 14, 2 -> 15, 9)
  }

  behavior of "P12"
  it should "be unique elements for duplicate list" in {
    P12.decode(List(1 -> 10, 2 -> 11, 1 -> 12, 3 -> 13, 1 -> 14, 2 -> 15, 1 -> 9)) shouldBe
      List(10, 11, 11, 12, 13, 13, 13, 14, 15, 15, 9)
  }

  behavior of "P13"
  it should "be unique elements for duplicate list" in {
    P13.encodeDirect(List(10, 11, 11, 12, 13, 13, 13, 14, 15, 15, 9)) shouldBe
      List(1 -> 10, 2 -> 11, 1 -> 12, 3 -> 13, 1 -> 14, 2 -> 15, 1 -> 9)
  }

  behavior of "P14"
  it should "be Nil for Nil" in {
    P14.duplicate(Nil) shouldBe Nil
  }

  behavior of "P15"
  it should "be Nil for Nil" in {
    P15.duplicateN(0,Nil) shouldBe Nil
  }

}
