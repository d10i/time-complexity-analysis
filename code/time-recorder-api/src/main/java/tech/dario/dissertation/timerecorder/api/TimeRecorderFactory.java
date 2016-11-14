package tech.dario.dissertation.timerecorder.api;

import tech.dario.dissertation.timerecorder.tree.Metrics;
import tech.dario.dissertation.timerecorder.tree.Tree;

public interface TimeRecorderFactory {
  void start();

  TimeRecorder getTimeRecorder();

  Tree<Metrics> stop() throws Exception;
}
