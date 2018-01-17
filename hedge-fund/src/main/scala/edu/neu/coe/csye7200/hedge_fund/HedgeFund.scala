package edu.neu.coe.csye7200.hedge_fund

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import edu.neu.coe.csye7200.hedge_fund.actors.{ExternalLookup, HedgeFundBlackboard, PortfolioUpdate}
import edu.neu.coe.csye7200.hedge_fund.model.{GoogleOptionQuery, GoogleQuery, Query, YQLQuery}
import edu.neu.coe.csye7200.hedge_fund.portfolio.{Portfolio, PortfolioParser}

import scala.io.Source

/**
 * @author robinhillyard
 */
object HedgeFund {

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()
    implicit val system = ActorSystem("HedgeFund")
    println(s"""${config.getString("name")}, ${config.getString("appVersion")}""")
    val engine: Option[Query] = config.getString("engine") match {
      case "YQL" => Some(YQLQuery(config.getString("format"), false))
      case "Google" => Some(GoogleQuery("NASDAQ"))
      case _ => None
    }
    engine match {
      case Some(x) =>
        val portfolio = getPortfolio(config)
        val blackboard = system.actorOf(Props.create(classOf[HedgeFundBlackboard]), "blackboard")
        val symbols = getSymbols(config,portfolio)
        blackboard ! ExternalLookup(x.getProtocol, x.createQuery(symbols))
        val optionEngine = new GoogleOptionQuery
        symbols foreach {
          s => blackboard ! ExternalLookup(optionEngine.getProtocol, optionEngine.createQuery(List(s)))
        }
        blackboard ! PortfolioUpdate(portfolio)

      case _ => println("initialization engine not defined")
    }
  }
  
import scala.language.postfixOps
  def getSymbols(config: Config, portfolio: Portfolio) = {
    // TODO add in the symbols from the portfolio
    config.getString("symbols") split ("\\,") toList;
  }

def getPortfolio(config: Config): Portfolio = {
   val json = Source.fromFile(config.getString("portfolio")) mkString
   val portfolio = PortfolioParser.decode(json)
   println(s"portfolio: $portfolio")
  portfolio
  }
}
