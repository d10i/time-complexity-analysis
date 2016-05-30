package tech.dario.dissertation.timerecorder.akka

case class TimeReport(elapsedTime: Long, stackTrace: Array[StackTraceElement])
