package tech.dario.dissertation.timerecorder.akka

import akka.actor.{Actor, ActorLogging}
import tech.dario.dissertation.timerecorder.tree.{MeasuredStackTraceElements, Metrics, Tree}

import scala.collection.mutable

class Worker extends Actor with ActorLogging {

  var queue = new mutable.Queue[TimeReport]
  val tree: Tree[Metrics] = new Tree[Metrics]

  override def postStop = {
    //log.info(s"$self: Worker stopped")
  }

  override def receive = {
    case tr@TimeReport(elapsedTime, stackTrace) =>
      //log.info(s"$self: Work starts")
      val measuredStackTraceElements = MeasuredStackTraceElements.fromStackTrace(stackTrace)
      //log.info(s"$measuredStackTraceElements: $elapsedTime")
      tree.add(Metrics.fromElapsedTime(elapsedTime), measuredStackTraceElements)
    //queue += tr
    //log.info(s"$self: Work ends")

    case s: Save =>
      sender() ! tree

    //log.info(s"$self: Worker Save")
    //log.info("{}", queue)
    //log.info(s"$self: Worker Save done")
  }
}
