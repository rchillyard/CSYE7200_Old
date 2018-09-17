/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200.asstll

/**
  * Trait to define the behavior of a LazyList (aka Stream)
  *
  * @tparam X the underlying type of this LazyList
  */
sealed trait LazyList[+X] {

  /**
    * @return the (strict) head of this stream
    */
  def head: X

  /**
    * @return the (non-strict) tail of this stream
    */
  def tail: LazyList[X]

  /**
    * Concatenate this LazyList with ys.
    *
    * @param ys the stream to be used if/when this stream is exhausted.
    * @tparam Y the underlying type of ys and the result.
    * @return a <code>LazyList[Y]<code> which contains all the elements of this followed by all the elements of ys.
    */
  def concat[Y >: X](ys: LazyList[Y]): LazyList[Y] = this match {
    case Cons(h, g) => Cons[Y](h, () => g() concat ys)
    case _ => ys
  }

  /**
    * The "map" function.
    * NOTE: that we have defined the <code>map</code> function in terms of <code>flatMap</code> and the
    * <code>apply</code> function (sometimes known as the "unit" function).
    *
    * @param f a function which converts an <code>X<code into a <code>Y<code>.
    * @tparam Y the underlying type of the result.
    * @return a <code>LazyList[Y]<code> where each element is the result of applying <code>f<code to the corresponding
    *         element of <code>this<code>.
    */
  def map[Y](f: X => Y): LazyList[Y] = flatMap(x => LazyList(f(x)))

  /**
    * The "flatMap" function.
    *
    * @param f a function which converts an <code>X<code into a <code>LazyList[Y]<code>.
    * @tparam Y the underlying type of the result.
    * @return a <code>LazyList[Y]<code> where each element is the result of applying <code>f<code to the corresponding
    *         element of <code>this<code> and then "flattening" the result
    *         by concatenating all streams together.
    */
  def flatMap[Y](f: X => LazyList[Y]): LazyList[Y] = this match {
    case Cons(h, g) => val y = f(h); Cons(y.head, () => y.tail concat g().flatMap(f))
    case _ => EmptyList
  }

  /**
    * The "filter" function.
    * @param p a predicate which takes an <code>X<code> and yields a <code>Boolean<code>.
    * @return a <code>LazyList[X]<code> where every element satisfies the predicate <code>p<code>.
    */
  def filter(p: X => Boolean): LazyList[X] = this match {
    case Cons(h, tailFunc) => val tail = tailFunc().filter(p); if (p(h)) Cons(h, () => tail) else tail
    case _ => EmptyList
  }

  /**
    * Method to "zip" to LazyList objects together
    *
    * @param ys the stream of Ys
    * @tparam Y the underlying type of <code>ys</code>
    * @return a <code>LazyList[(X,Y)]<code> where each element is a tuple of the corresponding elements from this
    *         and ys respectively.
    */
  def zip[Y](ys: LazyList[Y]): LazyList[(X, Y)] = this match {
    case Cons(x, f) => ys match {
      case Cons(y, g) => Cons((x, y), () => f() zip g())
      case _ => EmptyList
    }
    case _ => EmptyList
  }

  /**
    * Convert this LazyList into a <code>Seq[X]</code> by evaluating the first <code>n<code elements and discarding the
    * rest.
    * NOTE: that this is unlike the take method of Stream, which returns a Stream.
    *
    * @param n the number of elements to take (must not be negative).
    * @return a sequence of length n.
    */
  def take(n: Int): Seq[X] = n match {
    case 0 => Nil
    case _ =>
      if (n > 0)
        this match {
          case Cons(h, f) => h +: f().take(n - 1)
          case _ => Nil
        }
      else
        throw LazyListException("cannot take negative number of elements")
  }

  /**
    * ToString method.
    *
    * @return a String which shows the head but leaves the tail as question marks.
    */
  override def toString = s"$head, ???"
}

/**
  * Case class representing a non-empty LazyList.
  *
  * @param x        the head of the stream.
  * @param lazyTail a function which, when invoked, yields the tail of this stream.
  * @tparam X the underlying type of the stream, and of the value <code>x</code>.
  */
case class Cons[X](x: X, lazyTail: () => LazyList[X]) extends LazyList[X] {
  def head: X = x

  def tail = lazyTail()
}

/**
  * Case object representing an empty LazyList.
  */
case object EmptyList extends LazyList[Nothing] {
  def head = throw LazyListException("empty")

  def tail: LazyList[Nothing] = EmptyList
}

object LazyList {
  /**
    * Construct a (finite) <code>LazyList[X]</code> with exactly one element.
    *
    * @param x the value of the element.
    * @tparam X the underlying type of the result.
    * @return a <code>LazyList[X]</code> with exactly one element (whose value is <code>x</code>).
    */
  def apply[X](x: X): LazyList[X] = Cons[X](x, () => EmptyList)

  /**
    * Construct a (finite) <code>LazyList[X]</code> corresponding to a sequence.
    *
    * @param xs the sequence.
    * @tparam X the underlying type of the result.
    * @return a <code>LazyList[X]</code> with the same number of elements as <code>xs</code>.
    */
  def apply[X](xs: Seq[X]): LazyList[X] = xs match {
    case Nil => EmptyList
    case h :: t => Cons(h, () => apply(t))
  }

  /**
    * Construct a stream of xs.
    *
    * @param x the value to be repeated
    * @tparam X the type of X
    * @return a <code>LazyList[X]</code> with an infinite number of element (whose values are <code>x</code>).
    */
  def continually[X](x: X): LazyList[X] = Cons(x, () => continually(x))

  /**
    * A lazy val definition of a stream of 1s.
    */
  lazy val ones: LazyList[Int] = Cons(1, () => ones)

  /**
    * Construct a stream of Integers starting with <code>start</code> and with successive elements being
    * greater than their predecessors by <code>step</code>.
    *
    * @param start the value of the first element.
    * @param step  the difference between successive elements.
    * @return a <code>LazyList[X]</code> with an infinite number of element (whose values are <code>x</code>,
    *         <code>x+step</code>, etc.).
    */
  def from(start: Int, step: Int): LazyList[Int] = ??? // TODO implement me

  /**
    * Construct a stream of Integers starting with <code>start</code> and with successive elements being
    * the next greater Int.
    *
    * @param start the value of the first element.
    * @return a <code>LazyList[X]</code> with an infinite number of element (whose values are <code>x</code>,
    *         <code>x+1</code>, etc.).
    */
  def from(start: Int): LazyList[Int] = from(start, 1)
}

case class LazyListException(w: String) extends Exception(s"LazyList exception: $w")