package tech.dario.dissertation.timerecorder.api;

import tech.dario.dissertation.timerecorder.tree.MergeableTree;
import tech.dario.dissertation.timerecorder.tree.Metrics;

public interface TimeRecorder {
  void start();

  void reportTime(long elapsedTime, StackTraceElement[] stackTrace);

  MergeableTree<Metrics> stop() throws Exception;
}
