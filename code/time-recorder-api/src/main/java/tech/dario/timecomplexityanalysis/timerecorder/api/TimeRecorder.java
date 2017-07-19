package tech.dario.timecomplexityanalysis.timerecorder.api;

import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;

public interface TimeRecorder {
  void start() throws Exception;

  void methodStarted(String methodLongName);
  void methodFinished(String methodLongName);

  MergeableTree<Measurement> stop() throws Exception;
}
