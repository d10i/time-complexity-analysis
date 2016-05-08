package com.dariosimonetti.dissertation.agent

import javassist.NotFoundException

import akka.actor.Actor
import com.dariosimonetti.dissertation.agent.tree.{Metrics, TreeRoot}

class Worker extends Actor {
  var tree: TreeRoot[Metrics] = new TreeRoot[Metrics]

  def receive = {
    case TimeReport(elapsedTime, stackTrace) =>
      try {
        tree.add(Metrics.fromElapsedTime(elapsedTime), MeasuredStackTraceElements.fromStackTrace(stackTrace))
      }
      catch {
        case e: NotFoundException => {
          e.printStackTrace
        }
      }
    case Save(file) =>
      tree.serializeToFile(file)
  }
}