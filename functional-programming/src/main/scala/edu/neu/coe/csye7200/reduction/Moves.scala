package edu.neu.coe.csye7200.reduction

import scala.annotation.tailrec

/**
  * This is from a problem in LeetCode: 780 Reaching Points (Hard)
  */
trait Moves {
  /**
    * Method to determine if we can move successfully from point x
    *
    * @param x the point
    * @return true if there is such a path
    */
  def valid(x: Point): Boolean

  /**
    * This method defines the possible moves from point p
    *
    * @param p     the point
    * @param which the strategy to use
    * @return the point we moved to
    */
  def move(p: Point, which: Boolean): Point
}

case class Point(x: Int, y: Int) {
  override def toString: String = "Point{" + "x=" + x + ", y=" + y + '}'

  /**
    * Gets the non-Euclidean distance (we can't go in a straight line so this is proper) from this point to the target
    *
    * @param target our desired destination
    * @return the total number of steps we must take to get to target
    */
  def distance(target: Point): Int = target.x - x + target.y - y

  def valid: Boolean = x > 0 && y > 0
}

/**
  *
  * @param t The target point that we wish to reach
  */
case class Moves1(t: Point) extends Moves {

  def valid(p: Point): Boolean = inner(p :: Nil, result = false)

  @tailrec private def inner(points: List[Point], result: Boolean): Boolean = points match {
    case Nil => result
    case x :: remainder => x match {
      case `t` => true
      case _ =>
        if (x.x > t.x || x.y > t.y) inner(remainder, result = false)
        else {
          val addToY = move(x, which = true)
          val addToX = move(x, which = false)
          val work =
            if (addToY.distance(t) < addToX.distance(t))
              addToY +: remainder :+ addToX
            else
              addToX +: remainder :+ addToY
          inner(work, result)
        }
    }
  }

  override def move(p: Point, which: Boolean): Point = if (which) Point(p.x, p.x + p.y) else Point(p.x + p.y, p.y)
}

object Moves1 {
  def apply(x: Int, y: Int): Moves1 = apply(Point(x, y))
}

/**
  * In Moves2, we work backwards from the target to the start
  *
  * @param s the start point that we wish to reach
  */
case class Moves2(s: Point) extends Moves {

  def valid(t: Point): Boolean = {
    @tailrec
    def inner(p: Point): Boolean = if (p == s) true else if (!p.valid) false else inner({
      val x = move(p, true); println(x); x
    })

    inner(t)
  }


  /**
    * This method defines the possible moves from point p
    *
    * @param p     the point
    * @param which ignored
    * @return the point we moved to
    */
  override def move(p: Point, which: Boolean): Point = if (p.y > p.x) Point(p.x, p.y - p.x) else Point(p.x - p.y, p.y)
}

object Moves2 {
  def apply(x: Int, y: Int): Moves2 = apply(Point(x, y))
}
