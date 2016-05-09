package tech.dario.dissertation.timereporter.api;

public interface TimeReporter {
  void reportTime(long elapsedTime, Thread thread);
}
