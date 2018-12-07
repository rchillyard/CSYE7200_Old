package edu.neu.coe.csye7200.ga

import scala.util.Random

/**
  * This class simulates a set of events, each with particular odds of occurrence.
  * @param eventOdds a sequence of EventOdds[Event].
  * @tparam Event the underlying type of an event.
  */
case class Wheel[Event](eventOdds: Seq[EventOdds[Event]]) {
  private val outcomes = ??? //TODO

  private def lookup(i: Int): Event = ??? //TODO

  /**
    * Given a Random object, this method will "spin" the wheel and generate an event according to the probabilities
    * defined by the given odds.
    * @param r a source of random numbers
    * @return an Event
    */
  def spin(r: Random): Event = lookup(r.nextInt(outcomes))

}

object Wheel {
  /**
    * method to create a new Wheel based on a variable number of EventOdds instances.
    * @param xs the variable number of EventOdss instances.
    * @tparam Event the underlying event type.
    * @return a Wheel based on the given event odds.
    */
  def create[Event](r: Random, xs: EventOdds[Event]*): Wheel[Event] = Wheel(xs.toList)

  /**
    * method to create a new Wheel based on a variable number of EventOdds instances.
    * @param xs the variable number of EventOdss instances.
    * @tparam Event the underlying event type.
    * @return a Wheel based on the given event odds.
    */
  def create[Event](xs: EventOdds[Event]*): Wheel[Event] = Wheel(xs.toList)
}

/**
  * A case class which defines an Event and its odds.
  * @param event the event
  * @param odds the odds
  * @tparam Event the underlying type of the event.
  */
case class EventOdds[Event](event: Event, odds: Int)

object EventOdds {
  implicit def convertTupleToStringEvent(t: (String,Int)): EventOdds[String] = EventOdds(t._1,t._2)
  implicit def convertTupleToBooleanEvent(t: (Boolean,Int)): EventOdds[Boolean] = EventOdds(t._1,t._2)
}

case class LogicError(w: String) extends Exception(w)
