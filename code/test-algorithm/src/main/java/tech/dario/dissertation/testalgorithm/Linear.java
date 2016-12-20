package tech.dario.dissertation.testalgorithm;

import tech.dario.dissertation.agent.annotations.Measured;

public class Linear {
  // quick(1000) ~ 1ms ~ 3000000 iterations
  @Measured
  public void quick(long n) {
    long v = n * 3000L;
    for (long i = 0; i < v; i++) {

    }
  }

  // average(1000) ~ 10ms ~ 30000000 iterations
  @Measured
  public void average(long n) {
    long v = n * 30000L;
    for (long i = 0; i < v; i++) {

    }
  }

  // slow(1000) ~ 100ms ~ 300000000 iterations
  @Measured
  public void slow(long n) {
    long v = n * 300000L;
    for (long i = 0; i < v; i++) {

    }
  }
}
