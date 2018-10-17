package edu.neu.coe.csye7200.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit._
import edu.neu.coe.csye7200.model.Model
import org.scalatest._

import scala.io.Source
import scala.concurrent.duration._
import spray.http._
import spray.http.MediaTypes._

import scala.language.postfixOps
import spray.json.enrichString

/**
 * This specification really tests much of the HedgeFund app but because it particularly deals with
 * processing data from the YQL (Yahoo Query Language) using JSON, we call it by its given name.
 */
class JsonGoogleParserSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
    with WordSpecLike with Matchers with Inside with BeforeAndAfterAll {

  def this() = this(ActorSystem("JsonGoogleParserSpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  import scala.language.postfixOps
  val json: String = Source.fromFile(getClass.getResource("/googleExample.json").getPath) mkString

  "json read" in {
    import spray.json._
    val obj = JsonGoogleParser.fix(json).parseJson
    // TODO may have to work on this to make it generic
    obj shouldBe "[{\"e\":\"NASDAQ\",\"elt\":\"Jul 24, 7:15PM EDT\",\"s\":\"2\",\"ec\":\"+0.05\",\"cp_fix\":\"-0.53\",\"l_cur\":\"124.50\",\"ccol\":\"chr\",\"t\":\"AAPL\",\"el\":\"124.55\",\"yld\":\"1.67\",\"div\":\"0.52\",\"pcls_fix\":\"125.16\",\"el_cur\":\"124.55\",\"id\":\"22144\",\"ec_fix\":\"0.05\",\"l\":\"124.50\",\"el_fix\":\"124.55\",\"l_fix\":\"124.50\",\"ecp_fix\":\"0.04\",\"c_fix\":\"-0.66\",\"c\":\"-0.66\",\"eccol\":\"chg\",\"cp\":\"-0.53\",\"lt\":\"Jul 24, 4:08PM EDT\",\"ecp\":\"0.04\",\"lt_dts\":\"2015-07-24T16:08:30Z\",\"ltt\":\"4:08PM EDT\"},{\"e\":\"NASDAQ\",\"elt\":\"Jul 24, 6:34PM EDT\",\"s\":\"2\",\"ec\":\"+0.02\",\"cp_fix\":\"-0.92\",\"l_cur\":\"38.85\",\"ccol\":\"chr\",\"t\":\"YHOO\",\"el\":\"38.87\",\"yld\":\"\",\"div\":\"\",\"pcls_fix\":\"39.21\",\"el_cur\":\"38.87\",\"id\":\"658890\",\"ec_fix\":\"0.02\",\"l\":\"38.85\",\"el_fix\":\"38.87\",\"l_fix\":\"38.85\",\"ecp_fix\":\"0.06\",\"c_fix\":\"-0.36\",\"c\":\"-0.36\",\"eccol\":\"chg\",\"cp\":\"-0.92\",\"lt\":\"Jul 24, 4:08PM EDT\",\"ecp\":\"0.06\",\"lt_dts\":\"2015-07-24T16:08:28Z\",\"ltt\":\"4:08PM EDT\"}]"
  }

  "json conversion" in {
    val contentTypeText = ContentType(MediaTypes.`text/html`, HttpCharsets.`ISO-8859-1`)
    val entity = HttpEntity(contentTypeText, json.getBytes())
    val ok = JsonGoogleParser.decode(entity) match {
      case Right(x) =>
        x.seq.length should equal(2)
        val quotes = x.seq
        quotes.head.get("t") should matchPattern { case Some(Some("AAPL")) => }

      case Left(x) =>
        fail("decoding error: " + x)
    }
    ok shouldBe Succeeded
  }

  "send back" in {
    val blackboard = system.actorOf(Props.create(classOf[MockGoogleBlackboard], testActor), "blackboard")
    val contentType = ContentType(MediaTypes.`text/html`, HttpCharsets.`ISO-8859-1`)
    val entityParser = _system.actorOf(Props.create(classOf[EntityParser], blackboard), "entityParser")
    val entity = HttpEntity(contentType, json.getBytes())
    entityParser ! EntityMessage("json:GF", entity)
    val msg = expectMsgClass(3.seconds, classOf[QueryResponse])
    println("msg received: " + msg)
    msg should matchPattern {
      case QueryResponse("AAPL", _) =>
    }
    inside(msg) {
      case QueryResponse(_, attributes) => attributes.get("l") should matchPattern { case Some("124.50") => }
    }
  }
}

import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await

class MockGoogleUpdateLogger(blackboard: ActorRef) extends UpdateLogger(blackboard) {
  override def processStock(identifier: String, model: Model): Unit = {
    model.getKey("price") match {
      case Some(p) =>
        // sender is the MarketData actor
        val future = sender ? SymbolQuery(identifier, List(p))
        val result = Await.result(future, timeout.duration).asInstanceOf[QueryResponse]
        result.attributes foreach {
          case (k, v) =>
            log.info(s"$identifier attribute $k has been updated to: $v")
            blackboard ! result
        }
      case None => log.warning(s"'price' not defined in model")
    }
  }
}

class MockGoogleBlackboard(testActor: ActorRef) extends Blackboard(Map(classOf[KnowledgeUpdate] -> "marketData", classOf[SymbolQuery] -> "marketData", classOf[OptionQuery] -> "marketData", classOf[CandidateOption] -> "optionAnalyzer", classOf[Confirmation] -> "updateLogger"),
  Map("marketData" -> classOf[MarketData], "optionAnalyzer" -> classOf[OptionAnalyzer], "updateLogger" -> classOf[MockGoogleUpdateLogger])) {

  override def receive: PartialFunction[Any, Unit] =
    {
      case msg: Confirmation => msg match {
        // Cut down on the volume of messages
        case Confirmation("AAPL", _, _) => super.receive(msg)
        case _ =>
      }
      case msg: QueryResponse => testActor forward msg

      case msg => super.receive(msg)
    }
}

