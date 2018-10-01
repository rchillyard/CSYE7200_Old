/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200

import org.scalatest.concurrent._
import org.scalatest.{FlatSpec, Matchers}

import scala.util._

/**
  * @author scalaprof
  */
class MonadSpec extends FlatSpec with Matchers with Futures with ScalaFutures {

  import Functor._

  behavior of "map (1)"

  it should "transform List(1, 2) into List(2, 4)" in {
    ListFunctor.map(List(1, 2))(_ * 2) shouldBe List(2, 4)
  }

  it should "transform Some(1) into Some(2)" in {
    OptionFunctor.map(Some(1))(_ * 2) shouldBe Some(2)
  }

  it should "transform Success(1) into Success(2)" in {
    TryFunctor.map(Success(1))(_ * 2) shouldBe Success(2)
  }

  it should "transform List() into List()" in {
    ListFunctor.map(List[Int]())(_ * 2) shouldBe Nil
  }

  it should "transform None into None" in {
    OptionFunctor.map(Some(1))(_ * 2) shouldBe Some(2)
  }

  it should "transform Failure(_) into Failure(_)" in {
    val z: Try[Int] = Failure(new Exception("junk"))
    TryFunctor.map(z)(_ * 2) should matchPattern { case Failure(_) => }
  }

  import Monad._

  behavior of "map (2)"

  it should "transform 1, 2 and 2, 4 into 4, 5, 8, 10" in {
    ListMonad.map(List(1, 2), List(4, 5))(_ * _) shouldBe List(4, 5, 8, 10)
  }

  it should "transform 1, 2 and () into ()" in {
    ListMonad.map(List(1, 2), List[Int]())(_ * _) shouldBe Nil
  }

  it should "transform Some(1), Some(2) into Some(3)" in {
    OptionMonad.map(Some(1), Some(2))(_ + _) shouldBe Some(3)
  }

  it should "transform Some(1), None into None" in {
    OptionMonad.map(Some(1), None)(_ + _) shouldBe None
  }

  it should "transform None, Some(1) into None" in {
    val n: Option[Int] = None
    OptionMonad.map(n, Some(1))(_ + _) shouldBe None
  }

  it should "transform Success(1), Success(2) into Success(3)" in {
    TryMonad.map(Success(1), Success(2))(_ + _) shouldBe Success(3)
  }

  it should "transform Success(1), Failure(_) into Failure(_)" in {
    TryMonad.map(Success(1), Failure(new Exception("junk")))(_ + _) should matchPattern { case Failure(_) => }
  }

  it should "transform Failure, Success(1) into Failure" in {
    val n: Try[Int] = Failure(new Exception("junk"))
    TryMonad.map(n, Success(1))(_ + _) should matchPattern { case Failure(_) => }
  }

  behavior of "sequence"

  it should "result in None for List(1, 2, ...)" in {
    val ws = List("1", "2", "")
    val xos = for {w <- ws; xo = Try(w.toInt).toOption} yield xo
    Monad.sequence(xos) should matchPattern { case None => }
  }

  it should "result in Some(_) for List(1, 2, 3)" in {
    val ws = List("1", "2", "3")
    val xos = for {w <- ws; xo = Try(w.toInt).toOption} yield xo
    Monad.sequence(xos) should matchPattern { case Some(_) => }
  }

  it should "result in Failure(_) for List(1, 2, ...)" in {
    val ws = List("1", "2", "")
    val xos = for {w <- ws; xo = Try(w.toInt)} yield xo
    Monad.sequence(xos) should matchPattern { case Failure(_) => }
  }

  it should "result in Success(_) for List(1, 2, 3)" in {
    val ws = List("1", "2", "3")
    val xos = for {w <- ws; xo = Try(w.toInt)} yield xo
    Monad.sequence(xos) should matchPattern { case Success(_) => }
  }

}