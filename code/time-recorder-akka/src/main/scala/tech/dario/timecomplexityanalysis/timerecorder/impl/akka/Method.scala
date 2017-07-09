package tech.dario.timecomplexityanalysis.timerecorder.impl.akka

import scala.collection.immutable.Queue

sealed trait MethodAction {
  def nanoTime: Long
}

case class MethodActions(queue: Queue[MethodAction])

case class MethodStarted(methodLongName: String, nanoTime: Long) extends MethodAction
case class MethodFinished(methodLongName: String, nanoTime: Long) extends MethodAction

object MethodActions {
  def apply(methodActions: MethodAction*): MethodActions = {
    MethodActions(Queue(methodActions: _*))
  }

  def apply(methodActions: java.util.List[MethodAction]): MethodActions = {
    import collection.JavaConverters._
    MethodActions(Queue(methodActions.asScala: _*))
  }
}
