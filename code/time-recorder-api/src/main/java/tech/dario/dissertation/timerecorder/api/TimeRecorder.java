package tech.dario.dissertation.timerecorder.api;

public interface TimeRecorder {
  void start();

  void reportTime(long elapsedTime, StackTraceElement[] stackTrace);

  void stop();
}
