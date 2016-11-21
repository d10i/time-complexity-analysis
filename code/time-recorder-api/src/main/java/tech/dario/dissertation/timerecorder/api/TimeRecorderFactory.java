package tech.dario.dissertation.timerecorder.api;

import tech.dario.dissertation.timerecorder.tree.MetricsTree;

public interface TimeRecorderFactory {
  void start();

  TimeRecorder getTimeRecorder();

  MetricsTree stop() throws Exception;
}
