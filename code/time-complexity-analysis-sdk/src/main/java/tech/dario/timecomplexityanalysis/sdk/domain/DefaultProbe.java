package tech.dario.timecomplexityanalysis.sdk.domain;

import tech.dario.timecomplexityanalysis.sdk.domain.iterator.ExponentialIterator;

import java.util.Iterator;

public class DefaultProbe implements Probe {
  @Override
  public int getNumWarmUpRounds() {
    return 5;
    //return 2;
  }

  @Override
  public int getNumRecordingRounds() {
    return 40;
    //return 16;
  }

  @Override
  public int getNumMaxOutliers() {
    return 10;
    //return 4;
  }

  @Override
  public Iterator<Long> buildNIterator() {
    return new ExponentialIterator(8, 2097152);
    //return new LinearIterator(7, 286);
    //return new LinearIterator(8, 334);
    //return new LinearIterator(10, 429);
    //return new LinearIterator(8, 1000);
    //return new LinearIterator(2, 429);
  }

  @Override
  public float getFittingFunctionTolerance() {
    // 2%
    return 0.02f;
  }

  @Override
  public float getAssumeConstantThreshold() {
    // 100μs
    return 100000.0f;
  }

  @Override
  public float getAssumeConstantMaxRmsRelativeError() {
    // 20%
    return 0.2f;
  }
}
