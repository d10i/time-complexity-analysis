package tech.dario.timecomplexityanalysis.timerecorder.impl.akka

class TimeReportsBufferBuilder() {
  private var buffer: List[TimeReport] = List.empty

  def add(timeReport: TimeReport) = timeReport +: buffer

  def newInstance() = {
    buffer = List.empty
  }

  def build() = TimeReportsBuffer(buffer)
}

case class TimeReportsBuffer(buffer: List[TimeReport])

case class TimeReport(elapsedTime: Long, stackTrace: Array[StackTraceElement])
