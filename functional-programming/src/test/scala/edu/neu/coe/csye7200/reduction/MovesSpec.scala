package edu.neu.coe.csye7200.reduction

import org.junit.Assert.{assertFalse, assertTrue}
import org.scalatest.concurrent.{Futures, ScalaFutures}
import org.scalatest.{FlatSpec, Matchers}

class MovesSpec extends FlatSpec with Matchers with Futures with ScalaFutures {

  behavior of "distance"
  it should "be correct" in {
    Point(0,0).distance(Point(5,5)) shouldBe 10
    Point(0,1).distance(Point(10,5)) shouldBe 14
    Point(2,3).distance(Point(5,3)) shouldBe 3
    Point(3,2).distance(Point(3,5)) shouldBe 3
  }
  behavior of "Moves1"
  val start = Point(1, 1)
  it should "be true for 1,1->3,5" in {
    assertTrue(Moves1(3, 5).valid(start))
  }

  it should "be false for 1,1->2,2" in {
    assertFalse(Moves1(2, 2).valid(start))
  }

  it should "be true for 1,1->1,1" in {
    assertTrue(Moves1(1, 1).valid(start))
  }

  it should "be true for 1,1->99,100" in {
    assertTrue(Moves1(99, 100).valid(start))
  }

  ignore should "be true for 35,13->455955547,420098884" in {
    assertTrue(Moves1(455955547, 420098884).valid(start))
  }

  behavior of "Moves2"
  it should "be true for 1,1->3,5" in {
    assertTrue(Moves2(start).valid(Point(3,5)))
  }

  it should "be false for 1,1->2,2" in {
    assertFalse(Moves2(start).valid(Point(2,2)))
  }

  it should "be true for 1,1->1,1" in {
    assertTrue(Moves2(start).valid(start))
  }

  it should "be true for 1,1->99,100" in {
    assertTrue(Moves2(start).valid(Point(99,100)))
  }

  it should "be false for 35,13->455955547,420098884" in {
    assertFalse(Moves2(Point(35,13)).valid(Point(455955547, 420098884)))
  }

}

