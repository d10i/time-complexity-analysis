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
      insertionSort(methodActions.queue);

    case s: Save =>
      sender() ! queue.toList
  }

  private def insertionSort(otherQueue: Queue[MethodAction]) = {
    val (olderQueue, newerQueue) = queue.span(ma => otherQueue.head.nanoTime < ma.nanoTime)
    olderQueue ++ otherQueue ++ newerQueue
  }
}
