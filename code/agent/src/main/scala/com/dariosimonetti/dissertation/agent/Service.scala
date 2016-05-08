package com.dariosimonetti.dissertation.agent

import javassist.NotFoundException

import akka.actor._
import akka.routing.{Router, RoundRobinRoutingLogic, ActorRefRoutee}
import com.dariosimonetti.dissertation.agent.tree.{TreeRoot, Metrics}

class ServiceActor extends Actor {
  var tree: TreeRoot[Metrics] = new TreeRoot[Metrics]

  var router = {
    val routees = Vector.fill(14) {
      val r = context.actorOf(Props[Worker])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case TimeReportsBuffer(buffer: List[TimeReport]) =>
      for (timeReport <- buffer) router.route(timeReport, sender())
    //buffer.foreach(_ => router.route(_, sender()))
    case s @ Save(_) => router.route(s, sender())
  }
}