package tech.dario.dissertation.timecomplexityanalysis.sdk.domain;

import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.iterator.ExponentialIterator;
import java.util.Iterator;

public class DefaultProbe implements Probe {
  @Override
  public int getNumWarmUpRounds() {
    return 1;
  }

  @Override
  public int getNumRecordingRounds() {
    return 5;
  }

  @Override
  public int getNumMaxOutliers() {
    return 1;
  }

  @Override
  public Iterator<Long> buildNIterator() {
    return new ExponentialIterator(5, 500);
  }
}
