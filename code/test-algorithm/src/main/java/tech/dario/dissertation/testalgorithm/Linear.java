package tech.dario.dissertation.testalgorithm;

import tech.dario.dissertation.agent.annotations.Measured;

public class Linear {
  @Measured
  public void quick(int n) {
    for (long i = 0; i < n * 1562l; i++) {

    }
  }

  @Measured
  public void average(int n) {
    for (long i = 0; i < n * 6250l; i++) {

    }
  }

  @Measured
  public void slow(int n) {
    for (long i = 0; i < n * 25000l; i++) {

    }
  }
}
