package edu.neu.coe.csye7200

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import edu.neu.coe.csye7200.actors.{ExternalLookup, HedgeFundBlackboard, PortfolioUpdate}
import edu.neu.coe.csye7200.model.{GoogleOptionQuery, GoogleQuery, Query, YQLQuery}
import edu.neu.coe.csye7200.portfolio.{Portfolio, PortfolioParser}

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import scala.io.{BufferedSource, Source}
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
    println(s"""${config.getString("name")}, ${config.getString("appVersion")}""")
    implicit val system: ActorSystem = ActorSystem("HedgeFund")
    startup(config)
    Thread.sleep(10000)
    Await.ready(system.terminate(), FiniteDuration(1, "second"))
  }

  def startup(config: Config)(implicit system: ActorSystem): Try[ActorRef] = {
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
            Success(blackboard)

          case Failure(z) => Failure(z)
        }

      case _ => Failure(new Exception("initialization engine not defined"))
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
    val sy: Try[Source] = ???  // TODO
    val json = for (s <- sy) yield s.mkString
    json map PortfolioParser.decode
  }

}
