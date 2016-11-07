package tech.dario.dissertation.timerecorder.akka

import java.io._

import akka.actor.{Actor, ActorLogging}
import tech.dario.dissertation.timerecorder.akka.tree.{MeasuredStackTraceElements, Metrics, Tree}

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
      for (a <- 1 to 30000000) {

      }
      val measuredStackTraceElements: MeasuredStackTraceElements = MeasuredStackTraceElements.fromStackTrace(stackTrace)
      //log.info(s"$measuredStackTraceElements: $elapsedTime")
      tree.add(Metrics.fromElapsedTime(elapsedTime), measuredStackTraceElements)
    //queue += tr
    //log.info(s"$self: Work ends")

    case s: Save =>
      // log.info(s"$tree")
      val fileName: String = s"tree-${self.path.name}.ser"
      val fileOut = new FileOutputStream(fileName)
      val out = new ObjectOutputStream(fileOut)
      out.writeObject(tree)
      out.close()
      fileOut.close()
      try {
        val fileIn = new FileInputStream(fileName)
        val in = new ObjectInputStream(fileIn)
        val t = in.readObject().asInstanceOf[Tree[Metrics]]
        in.close()
        fileIn.close()
        log.info(s"$t")
      } catch {
        case i: IOException => i.printStackTrace()
        case c: ClassNotFoundException =>
          System.out.println("Tree class not found")
          c.printStackTrace()
      }

    //log.info(s"$self: Worker Save")
    //log.info("{}", queue)
    //log.info(s"$self: Worker Save done")
  }
}
