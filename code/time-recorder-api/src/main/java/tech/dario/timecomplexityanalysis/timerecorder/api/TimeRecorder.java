package tech.dario.timecomplexityanalysis.timerecorder.api;

import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Metrics;

public interface TimeRecorder {
  void start();

  void reportTime(long elapsedTime, StackTraceElement[] stackTrace);

  MergeableTree<Metrics> stop() throws Exception;
}
