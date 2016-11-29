package tech.dario.dissertation.testalgorithm;

import tech.dario.dissertation.agent.annotations.Measured;

public class Quadratic {
  @Measured
  public void quick(long n) {
    for (long i = 0; i < Math.pow(n, 2) * 1562l; i++) {

    }
  }

  @Measured
  public void average(long n) {
    for (long i = 0; i < Math.pow(n, 2) * 625l; i++) {

    }
  }

  @Measured
  public void slow(long n) {
    for (long i = 0; i < Math.pow(n, 2) * 2500l; i++) {

    }
  }
}
