package tech.dario.dissertation.testalgorithm;

import tech.dario.dissertation.agent.annotations.Measured;

public class Linearithmic {
  @Measured
  public void quick(long n) {
    for (long i = 0; i < n * Math.log(n) * 1562l; i++) {

    }
  }

  @Measured
  public void average(long n) {
    for (long i = 0; i < n * Math.log(n) * 6250l; i++) {

    }
  }

  @Measured
  public void slow(long n) {
    for (long i = 0; i < n * Math.log(n) * 25000l; i++) {

    }
  }
}
