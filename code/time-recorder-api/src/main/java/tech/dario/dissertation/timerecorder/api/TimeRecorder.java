package tech.dario.dissertation.timerecorder.api;

import tech.dario.dissertation.timerecorder.tree.Metrics;
import tech.dario.dissertation.timerecorder.tree.Tree;

public interface TimeRecorder {
  void start();

  void reportTime(long elapsedTime, StackTraceElement[] stackTrace);

  Tree<Metrics> stop() throws Exception;
}
