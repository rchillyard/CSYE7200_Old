package edu.neu.coe.csye7200.hedge_fund.http

import spray.http._
import spray.client.pipelining._

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{Actor, ActorRef, ActorSystem, Props}


import scala.concurrent._

import edu.neu.coe.csye7200.hedge_fund.actors.HttpResult

/**
 * CONSIDER making this an Actor
 * @author robinhillyard
 */
case class HttpTransaction(queryProtocol: String, request: HttpRequest, actor: ActorRef) {
  import akka.pattern.pipe

  implicit val system = ActorSystem()

  val pipeline: HttpRequest => Future[HttpResponse] = sendReceive

  val response: Future[HttpResponse] = pipeline(request)

  response map { x => HttpResult(queryProtocol, request, x) } pipeTo actor

}

