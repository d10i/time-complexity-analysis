package tech.dario.dissertation.timerecorder.api;

import tech.dario.dissertation.timerecorder.tree.MetricsTree;

public interface TimeRecorder {
  void start();

  void reportTime(long elapsedTime, StackTraceElement[] stackTrace);

  MetricsTree stop() throws Exception;
}
