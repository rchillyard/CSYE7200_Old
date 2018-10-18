package edu.neu.coe.csye7200.actors

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import edu.neu.coe.csye7200.model.{MapUtils, Model}
import edu.neu.coe.csye7200.rules.{Candidate, Predicate, Rule}

import scala.io.Source
import scala.language.postfixOps

/**
  * @author robinhillyard
  */
class OptionAnalyzer(blackboard: ActorRef) extends BlackboardActor(blackboard) {

  // This is mutable state information.
  // When this actor is terminated and reborn, the rules/properties will be re-read from their respective files
  var rules: Map[String, Predicate] = Map[String, Predicate]()
  var properties: List[Map[String, Any]] = List[Map[String, Any]]()

  override def receive: PartialFunction[Any, Unit] = {
    case CandidateOption(model, identifier, put, optionDetails, chainDetails) =>
      log.info("Option Analysis of identifier: {}", identifier)
      val candidate = OptionCandidate(put, model, identifier, optionDetails, chainDetails)
      if (applyRules(put, candidate)) {
        log.debug("Qualifies: sending confirmation message to blackboard")
        val attributes = MapUtils.flatten[String, Any](List("underlying") map { k => k -> candidate(k) } toMap)
        log.debug(s"attributes: $attributes")
        blackboard ! Confirmation(identifier, model, attributes)
      } else
        log.debug(s"$identifier does not qualify")

    case m => super.receive(m)
  }

  override def preStart() {
    rules = OptionAnalyzer.getRules(log)
    properties = OptionAnalyzer.getProperties
  }

  def getProperty(key: String, value: Any, property: String): Option[Any] =
    getProperties(key, value) match {
      case Some(m) => m.get(property);
      case None => None
    }

  def getProperties(key: String, value: Any): Option[Map[String, Any]] =
    properties find { p => p.get(key) match {
      case Some(`value`) => true;
      case _ => false
    }
    }

  def applyRules(put: Boolean, candidate: Candidate): Boolean = {
    candidate("underlying") match {
      case Some(u) =>
        val candidateWithProperties = candidate ++ (getProperties("Id", u) match {
          case Some(p) => p;
          case _ => Map()
        })
        val key = if (put) "put" else "call"
        rules.get(key) match {
          case Some(r) => r.apply(candidateWithProperties) match {
            case Right(b) => b
            case Left(e) => log.error("rules problem: {}", e); false
          }
          case None => log.error(s"rules problem: $key doesn't define a rule"); false
        }
      case _ => println(s"underlying is not defined for option: $candidate"); false
    }
  }
}

object OptionAnalyzer {

  import java.io.File

  import com.typesafe.config._

  def getRules(log: LoggingAdapter): Map[String, Predicate] = {
    val userHome = System.getProperty("user.home")
    val sRules = "rules.txt"
    val sUserRules = s"$userHome/$sRules"
    val sSysRules = s"src/main/resources/$sRules"
    val userRules = new File(sUserRules)
    val config = if (userRules.exists) ConfigFactory.parseFile(userRules) else ConfigFactory.parseFile(new File(sSysRules))
    List("put", "call") map { k => {
      val r = config.getString(k)
      log.info(s"rule: $k -> $r")
      k -> Rule(r)
    }
    } toMap
  }

  def getProperties: List[Map[String, String]] = {
    val sProperties = "properties.txt"
    val sSysProperties = s"src/main/resources/$sProperties"
    val src = Source.fromFile(sSysProperties).getLines
    // First line is the header
    val headerLine = src.take(1).next
    val columns = headerLine.split(",")
    src map { l => columns zip l.split(",") toMap } toList
  }
}

/**
  * @author robinhillyard
  *
  *         CONSIDER combining optionDetails and chainDetails in the caller
  */
case class OptionCandidate(put: Boolean, model: Model, id: String, optionDetails: Map[String, String], chainDetails: Map[String, Any]) extends Candidate {

  val details: Map[String, Any] = Map("put" -> put) ++ chainDetails ++ optionDetails

  def identifier: String = id

  // CONSIDER getting rid of the identifier case since we now have a method for that
  def apply(s: String): Option[Any] = s match {
    case "identifier" => Some(id)
    case _ => model.getKey(s) match {
      case Some(x) => details.get(x)
      case _ => None
    }
  }

  def ++(m: Map[String, Any]) = OptionCandidate(put, model, identifier, optionDetails, chainDetails ++ m)

  override def toString = s"OptionCandidate: identifier=$identifier; details=$details"
}
