package edu.neu.coe.csye7200

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import edu.neu.coe.csye7200.actors.{ExternalLookup, HedgeFundBlackboard, PortfolioUpdate}
import edu.neu.coe.csye7200.model.{GoogleOptionQuery, GoogleQuery, Query, YQLQuery}
import edu.neu.coe.csye7200.portfolio.{Portfolio, PortfolioParser}

import scala.io.Source
import scala.language.implicitConversions
import scala.util._

/**
  * @author robinhillyard
  *
  *         TODO migrate entire package from spray http to akka http
  */
object HedgeFund {

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()
    implicit val system: ActorSystem = ActorSystem("HedgeFund")
    println(s"""${config.getString("name")}, ${config.getString("appVersion")}""")
    val engine: Option[Query] = config.getString("engine") match {
      case "YQL" => Some(YQLQuery(config.getString("format"), diagnostics = false))
      case "Google" => Some(GoogleQuery("NASDAQ"))
      case _ => None
    }
    engine match {
      case Some(x) =>
        getPortfolio(config) match {
          case Success(portfolio) =>
            val blackboard = system.actorOf(Props.create(classOf[HedgeFundBlackboard]), "blackboard")
            val symbols = getSymbols(config, portfolio)
            blackboard ! ExternalLookup(x.getProtocol, x.createQuery(symbols))
            val optionEngine = new GoogleOptionQuery
            symbols foreach {
              s => blackboard ! ExternalLookup(optionEngine.getProtocol, optionEngine.createQuery(List(s)))
            }
            blackboard ! PortfolioUpdate(portfolio)

          case Failure(z) => System.err.println(z.getLocalizedMessage)
        }

      case _ => System.err.println("initialization engine not defined")
    }
  }

  import scala.language.postfixOps

  def getSymbols(config: Config, portfolio: Portfolio): List[String] = {
    // TODO add in the symbols from the portfolio
    config.getString("symbols") split "\\," toList
  }

  def getPortfolio(config: Config): Try[Portfolio] = {
    val file = config.getString("portfolio")
    // NOTE: we try to different ways of getting the file:
    // (1) where file is a pure filename relative to the filing system;
    // (2) where file is the name of a resource relative to the current class.
    val sy = Try(Source.fromFile(file)) orElse(Try(Source.fromURL(getClass.getResource(file))))
    val json = for (s <- sy) yield s.mkString
    json map PortfolioParser.decode
  }

}
