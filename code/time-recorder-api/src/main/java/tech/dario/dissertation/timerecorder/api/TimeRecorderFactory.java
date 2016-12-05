package tech.dario.dissertation.timerecorder.api;

import tech.dario.dissertation.timerecorder.tree.MergeableTree;
import tech.dario.dissertation.timerecorder.tree.Metrics;

public interface TimeRecorderFactory {
  void start();

  TimeRecorder getTimeRecorder();

  MergeableTree<Metrics> stop() throws Exception;
}
