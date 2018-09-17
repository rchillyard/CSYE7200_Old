/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200.laziness

import org.scalatest.{FlatSpec, Matchers}


class LazyListSpec  extends FlatSpec with Matchers {

  behavior of "Cons"
  it should "produce a single of 1" in {
    val x: LazyList[Int] = Cons(1, () => EmptyList)
    x.head shouldBe 1
    x.tail shouldBe EmptyList
  }

  it should "produce a stream of xs using Cons directly" in {
    lazy val x: LazyList[String] = Cons("x", () => x)
    val y = x take 3
    y.size shouldBe 3
    y.head shouldBe "x"
    y shouldBe Seq("x","x","x")
  }

  behavior of "ones"
  it should "produce a stream of 1s" in {
    val x: LazyList[Int] = LazyList.ones
    val y = x take 3
    y.size shouldBe 3
    y.head shouldBe 1
    y shouldBe Seq(1,1,1)
  }

  behavior of "take"
  it should "take zero from a finite stream" in {
    LazyList(1) take 0 shouldBe Nil
  }

  it should "take zero from an infinite stream" in {
    LazyList.continually(1) take 0 shouldBe Nil
  }

  it should "take 3 from a finite stream of actual length 1" in {
    LazyList(1) take 3 shouldBe Seq(1)
  }

  it should "take 3 from an infinite stream" in {
    LazyList.continually(1) take 3 shouldBe Seq(1,1,1)
  }

  behavior of "concat"
  it should "join two Empty streams together" in {
    val x: LazyList[Nothing] = EmptyList
    val y = x.concat(EmptyList)
    y shouldBe x
  }
  it should "join a stream with an Empty stream" in {
    val empty = EmptyList
    val ones = LazyList.continually(1)
    val y = ones.concat(empty)
    val z = y.take(3)
    z.size shouldBe 3
    z shouldBe Seq(1,1,1)
  }
  it should "join an Empty stream with a stream" in {
    val x: LazyList[Int] = EmptyList.asInstanceOf[LazyList[Int]]
    val ones = LazyList.continually(1)
    val y = x.concat(ones)
    y take 3 shouldBe Seq(1,1,1)
  }

  behavior of "map"
  it should "produce a stream of 2s" in {
    lazy val x: LazyList[Int] = Cons(1, () => x)
    val y = x map (_ * 2)
    assert(y.head==2)
    assert(y.tail.head==2)
    y take 4 shouldBe Seq(2,2,2,2)
  }

  behavior of "flatMap"
  it should "produce a stream of 2s from a single element 1" in {
    val x = LazyList(1)
    val y = x flatMap ( z => LazyList.continually(z * 2))
    assert(y.head==2)
    assert(y.tail.head==2)
    y take 4 shouldBe Seq(2,2,2,2)
  }

  it should "produce a stream of 2s from a stream of 1s" in {
    lazy val x: LazyList[Int] = Cons(1, () => x)
    val y = x flatMap ( z => LazyList.continually(z * 2))
    assert(y.head==2)
    assert(y.tail.head==2)
    y take 4 shouldBe Seq(2,2,2,2)
  }

  behavior of "zip"
  it should "zip together two empty streams" in {
    EmptyList.zip(EmptyList) shouldBe EmptyList
  }
  it should "zip together a stream and an empty stream" in {
    LazyList.continually(1).zip(EmptyList) shouldBe EmptyList
  }
  it should "zip together an empty stream and a stream" in {
    EmptyList.zip(LazyList.continually(1)) shouldBe EmptyList
  }
  it should "zip together two non-empty streams" in {
    val x = LazyList.from(1).zip(LazyList.from(2))
    x.head shouldBe (1,2)
    x.tail.head shouldBe (2,3)
  }

  behavior of "apply"
  it should "produce a stream of a single 1" in {
    val y = LazyList(1) take 3
    y shouldBe Seq(1)
  }

  behavior of "continually"
  it should "produce a stream of 1s" in {
    val y = LazyList.continually(1) take 3
    y shouldBe Seq(1,1,1)
  }

  it should "produce a stream of 1 thru 3" in {
    val x: LazyList[Int] = LazyList.from(1)
    val y = x take 3
    y shouldBe Seq(1,2,3)
  }

  behavior of "LazyList as a monad"
  it should "support a for-comprehension" in {
    val zs = for (x <- LazyList.from(1); y <- LazyList(Seq(1, 2, 3))) yield (x, y)
    zs take 5 shouldBe Seq(1->1, 1->2, 1->3, 2->1, 2->2)
  }
}
