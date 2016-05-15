package tech.dario.dissertation.testalgorithm;

import tech.dario.dissertation.agent.annotations.Measured;

public class Cubic {
  @Measured
  public void quick(int n) {
    for (long i = 0; i < Math.pow(n, 3) * 1562l; i++) {

    }
  }

  @Measured
  public void average(int n) {
    for (long i = 0; i < Math.pow(n, 3) * 6250l; i++) {

    }
  }

  @Measured
  public void slow(int n) {
    for (long i = 0; i < Math.pow(n, 3) * 25000l; i++) {

    }
  }
}
