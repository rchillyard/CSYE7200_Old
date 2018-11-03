/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200

import org.scalatest.{FlatSpec, Matchers}

class ListsSpec extends FlatSpec with Matchers {

  private val fib5 = List(1, 1, 2, 3, 5, 8)

  behavior of "P01"
  it should "get 8" in {
    P01.last(fib5) shouldBe 8
  }

  behavior of "P02"
  it should "get 5" in {
    P02.penultimate(fib5) shouldBe 5
  }

  behavior of "P03"
  it should "get 3" in {
    P03.kth(3, fib5) shouldBe 3
  }

  behavior of "P04"
  it should "get 6" in {
    P04.length(fib5) shouldBe 6
  }
}
