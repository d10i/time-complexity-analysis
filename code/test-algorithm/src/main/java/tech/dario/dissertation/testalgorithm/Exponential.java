package tech.dario.dissertation.testalgorithm;

import tech.dario.dissertation.agent.annotations.Measured;

public class Exponential {
  @Measured
  public void quick(long n) {
    for (long i = 0; i < Math.pow(2, n) * 1562l; i++) {

    }
  }

  @Measured
  public void average(long n) {
    for (long i = 0; i < Math.pow(2, n) * 6250l; i++) {

    }
  }

  @Measured
  public void slow(long n) {
    for (long i = 0; i < Math.pow(2, n) * 25000l; i++) {

    }
  }
}
