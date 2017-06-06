package tech.dario.timecomplexityanalysis.sdk.domain;

import tech.dario.timecomplexityanalysis.sdk.domain.iterator.LinearIterator;

import java.util.Iterator;

public class DefaultProbe implements Probe {
  @Override
  public int getNumWarmUpRounds() {
    return 4;
  }

  @Override
  public int getNumRecordingRounds() {
    return 20;
  }

  @Override
  public int getNumMaxOutliers() {
    return 4;
  }

  @Override
  public Iterator<Long> buildNIterator() {
    return new LinearIterator(10, 429);
  }
}
