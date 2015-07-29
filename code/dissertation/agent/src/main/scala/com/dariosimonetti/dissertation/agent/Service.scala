package com.dariosimonetti.dissertation.agent

import javassist.NotFoundException

import akka.actor._
import akka.routing.{Router, RoundRobinRoutingLogic, ActorRefRoutee}
import com.dariosimonetti.dissertation.tree.{TreeRoot, Metrics}

class ServiceActor extends Actor {
  var tree: TreeRoot[Metrics] = new TreeRoot[Metrics]

  /*var router = {
    val routees = Vector.fill(1) {
      val r = context.actorOf(Props[Worker])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }*/

  def receive = {
    case TimeReportsBuffer(buffer) => {
      buffer.map { timeReport =>
        try {
          println(timeReport.elapsedTime)
          tree.add(Metrics.fromElapsedTime(timeReport.elapsedTime), MeasuredStackTraceElements.fromStackTrace(timeReport.stackTrace))
        }
        catch {
          case e: NotFoundException => {
            e.printStackTrace()
          }
        }
      }
    }

    case TimeReport(elapsedTime, stackTrace) =>
      try {
        println(elapsedTime)
        tree.add(Metrics.fromElapsedTime(elapsedTime), MeasuredStackTraceElements.fromStackTrace(stackTrace))
      }
      catch {
        case e: NotFoundException => {
          e.printStackTrace()
        }
      }
    case Save(file) => {
      println(tree.toString)
      tree.serializeToFile(file)
    }
  }
}