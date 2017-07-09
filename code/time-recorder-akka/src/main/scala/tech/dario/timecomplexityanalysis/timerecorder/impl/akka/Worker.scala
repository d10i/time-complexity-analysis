package tech.dario.timecomplexityanalysis.timerecorder.impl.akka

import akka.actor.{Actor, ActorLogging}

import scala.collection.immutable.Queue

class Worker extends Actor with ActorLogging {

  var queue = Queue[MethodAction]()

  override def postStop = {
    //log.info(s"$self: Worker stopped")
  }

  override def receive = {
    case methodActions: MethodActions =>
      queue = queue ++ methodActions.queue

    case s: Save =>
      sender() ! queue.toList
  }
}
