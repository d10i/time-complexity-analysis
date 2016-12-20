package tech.dario.dissertation.timecomplexityanalysis.sdk.domain;

import java.util.Iterator;

public interface Probe {
  int getNumWarmUpRounds();
  int getNumRecordingRounds();
  int getNumMaxOutliers();
  Iterator<Long> buildNIterator();
}
