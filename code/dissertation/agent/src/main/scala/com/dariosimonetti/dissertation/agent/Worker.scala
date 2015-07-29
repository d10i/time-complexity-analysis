package com.dariosimonetti.dissertation.agent

import javassist.NotFoundException

import akka.actor.Actor
import com.dariosimonetti.dissertation.tree.{Metrics, TreeRoot}

class Worker extends Actor {
  var tree: TreeRoot[Metrics] = new TreeRoot[Metrics]

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
