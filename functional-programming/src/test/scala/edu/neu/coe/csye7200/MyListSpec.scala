package edu.neu.coe.csye7200

import org.scalatest.{FlatSpec, Matchers}

/**
  * @author scalaprof
  */
class MyListSpec extends FlatSpec with Matchers {

  "Nil" should "have zero length" in {
    MyNil.length should be(0)
  }
  it should "equal Nil" in {
    MyNil.equals(MyNil) shouldBe true
  }
  it should "be empty" in {
    MyNil.isEmpty shouldBe true
  }
  it should "have Nil tail" in {
    MyNil.x3 shouldBe MyNil
  }
  it should "should throw an exception on get" in {
    val x: MyList[Nothing] = MyNil
    an[IndexOutOfBoundsException] should be thrownBy x.apply(0)
  }
  it should "leave any list unchanged on prepend" in {
    val x = MyList(1, 2, 3) // arbitrary
    MyCons(MyNil, x).equals(x) shouldBe true
  }
  it should "leave any list unchanged on append" in {
    val x = MyList(1, 2, 3) // arbitrary
    (x ++ MyNil).equals(x) shouldBe true
  }

  "IList(1,2,3)" should "have 3 length" in {
    MyList(1, 2, 3).length should be(3)
  }
  it should "equal IList(1,2,3)" in {
    MyList(1, 2, 3).equals(MyList(1, 2, 3)) shouldBe true
  }
  it should "remain unchanged on addition of Nil" in {
    val x = MyList(1, 2, 3)
    MyCons(MyNil, x).equals(x) shouldBe true
  }
  it should "not be empty" in {
    MyList(1, 2, 3).isEmpty shouldBe false
  }
  it should "have Nil tail" in {
    MyList(1, 2, 3).x3 should be(MyList(2, 3))
  }
  it should "be 3 on x4(2)" in {
    val x: MyList[Int] = MyList(1, 2, 3)
    x.apply(2) should be(3)
  }
  it should "be IList(1,2,3) on map" in {
    val x: MyList[Int] = MyList(1, 2, 3)
    x.map({
      _.toString
    }) shouldBe MyList("1", "2", "3")
  }
  it should "be List(1,2,3) on flatMap" in {
    val x: MyList[Int] = MyList(1, 2, 3)
    x.flatMap({ e => MyList(e.toString) }) shouldBe MyList("1", "2", "3")
  }
  it should "be IList(1,2,3,4,5,6) on ++" in {
    val x: MyList[Int] = MyList(1, 2, 3)
    val y: MyList[Int] = MyList(4, 5, 6)
    (x ++ y) shouldBe MyList(1, 2, 3, 4, 5, 6)
  }
  it should "have length 2 on ++" in {
    val x: MyList[CharSequence] = MyList(new StringBuffer("A"))
    val y: MyList[String] = MyList("B")
    (x ++ y).length should be(2)
  }
  it should "have length 2 on ++ (2)" in {
    val x: MyList[CharSequence] = MyList(new StringBuffer("A"))
    val y: MyList[String] = MyList("B")
    (y ++ x).length should be(2)
  }
}