package edu.neu.coe.csye7200.assthw

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by scalaprof on 8/25/16.
  */
class HelloWorldSpec extends FlatSpec with Matchers {
  behavior of "HelloWorld"
  it should "get the correct greeting" in {
    HelloWorld.greeting shouldBe "Hello World!"
  }
}