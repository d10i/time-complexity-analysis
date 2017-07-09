package tech.dario.timecomplexityanalysis.timerecorder.impl.singlethread

sealed trait MethodAction {
  def nanoTime: Long
}

case class MethodStarted(methodLongName: String, nanoTime: Long) extends MethodAction
case class MethodFinished(methodLongName: String, nanoTime: Long) extends MethodAction
