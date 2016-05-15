package tech.dario.dissertation.timerecorder.api;

public interface TimeRecorder {
  void reportTime(long elapsedTime, Thread thread);
}
