package tech.dario.dissertation.timerecorder.akka

import akka.actor.{Actor, ActorLogging}

import scala.collection.mutable

class Worker extends Actor with ActorLogging {

  var queue = new mutable.Queue[TimeReport]

  override def postStop = {
    //log.info(s"$self: Worker stopped")
  }

  override def receive = {
    case tr @ TimeReport(elapsedTime, stackTrace) =>
      //log.info(s"$self: Work starts")
      for (a <- 1 to 30000000) {

      }
      queue += tr
      //log.info(s"$self: Work ends")

    case s: Save =>
      //log.info(s"$self: Worker Save")
      //log.info("{}", queue)
      //log.info(s"$self: Worker Save done")
  }
}
