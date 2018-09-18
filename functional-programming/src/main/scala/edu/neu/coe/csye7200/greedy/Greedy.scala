/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200.greedy

import java.util.function.{BiFunction, Function}

import scala.annotation.tailrec

case class Greedy[T, R](fGreedy: T => T, fAdjust: (T, T) => T, fResult: (T, R) => R, fDone: T => Boolean) extends ((T, R) => R) {
  override def apply(t: T, r: R): R = {

    @tailrec def inner(_t: T, _r: R): R =
      if (fDone(_t)) _r
      else {
        val greedy = fGreedy.apply(_t)
        inner(fAdjust.apply(_t, greedy), fResult.apply(greedy, _r))
      }

    inner(t, r)
  }
}
