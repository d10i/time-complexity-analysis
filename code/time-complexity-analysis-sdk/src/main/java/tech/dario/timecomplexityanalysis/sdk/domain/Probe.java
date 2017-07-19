package tech.dario.timecomplexityanalysis.sdk.domain;

import java.util.Iterator;

public interface Probe {
  int getNumWarmUpRounds();
  int getNumRecordingRounds();
  int getNumMaxOutliers();
  Iterator<Long> buildNIterator();
  float getFittingFunctionTolerance();
  float getAssumeConstantThreshold();
  float getAssumeConstantMaxRmsRelativeError();
}
