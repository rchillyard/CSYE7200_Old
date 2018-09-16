/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200.laziness

import org.scalatest.{FlatSpec, Matchers}


class MyStreamSpec  extends FlatSpec with Matchers {

  behavior of "MyStream"

  it should "produce a stream of 1s" in {
    lazy val x: MyStream[Int] = Cons(1,  () => x)

    val y = x take 3
    assert(y.size==3)
  }
}
