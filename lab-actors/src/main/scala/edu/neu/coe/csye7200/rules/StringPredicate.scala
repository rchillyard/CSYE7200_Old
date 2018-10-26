package edu.neu.coe.csye7200.rules

import scala.util.{Failure, Try, _}

/**
  * @author robinhillyard
  */
case class StringPredicate(variable: String, operator: Operator[String], value: String) extends Predicate {

  def apply(candidate: Candidate): Either[Throwable, Boolean] = candidate(variable) match {
    case Some(x) => Try {
      operator(x.toString, value)
    } match {
      case Success(v) => Right(v)
      case Failure(f) => Left(f)
    }
    case _ => Left(new Exception(s"variable $variable not found in $candidate"))
  }
}

object StringPredicate {
  def apply(variable: String, operator: String, value: String): StringPredicate =
    new StringPredicate(variable, Operator.createText(operator), value)
}