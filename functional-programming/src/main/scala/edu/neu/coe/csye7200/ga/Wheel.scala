package edu.neu.coe.csye7200.ga

import scala.util.Random

case class Wheel(eventOdds: Seq[EventOdds]) {
  private val outcomes = (for (x <- eventOdds) yield x.odds) sum

  private def lookup(i: Int): String = {
    def inner(es: Seq[EventOdds], x: Int): String = es match {
      case Nil => throw LogicError(s"cannot get event for $i in $this")
      case h :: t => if (x < h.odds) h.event else inner(t, x - h.odds)
    }
    inner(eventOdds, i)
  }

  def spin(r: Random): String = lookup(r.nextInt(outcomes))
}

object Wheel {
  def create(xs: EventOdds*): Wheel = Wheel(xs.toList)
}

case class EventOdds(event: String, odds: Int)

object EventOdds {
  implicit def convertTuple(t: (String,Int)): EventOdds = EventOdds(t._1,t._2)
}

case class LogicError(w: String) extends Exception(w)
