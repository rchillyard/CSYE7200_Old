/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200.parse

import org.scalatest.{FlatSpec, Matchers}

class SubstringSpec extends FlatSpec with Matchers {

  import Substring._


  behavior of "substring"

  private val alphabet = "abcdefg"

  it should "pass the tests involving empty prefix" in {
    substring("","") shouldBe true
    substring("",alphabet) shouldBe true
  }
  it should "pass the tests involving empty string" in {
    substring(alphabet,"") shouldBe false
  }
  it should "pass the tests involving prefix strings" in {
    substring("a",alphabet) shouldBe true
    substring("ab",alphabet) shouldBe true
    substring("abc", alphabet) shouldBe true
  }
  it should "pass the tests involving internal strings" in {
    substring("cde",alphabet) shouldBe true
    substring("bce",alphabet) shouldBe true
    substring("def", alphabet) shouldBe true
  }
  it should "pass the tests involving suffix strings" in {
    substring("efg",alphabet) shouldBe true
    substring("fg",alphabet) shouldBe true
    substring("g", alphabet) shouldBe true
  }
  it should "pass the tests involving no sub-strings" in {
    substring("xyz",alphabet) shouldBe true
    substring("yz",alphabet) shouldBe true
    substring("z", alphabet) shouldBe true
  }
}
