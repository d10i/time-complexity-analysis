package tech.dario.dissertation.agenttest;

import tech.dario.dissertation.agent.annotations.Measured;

public class Exponential {
  @Measured
  public void quick(int n) {
    for (long i = 0; i < Math.pow(2, n) * 1562l; i++) {

    }
  }

  @Measured
  public void average(int n) {
    for (long i = 0; i < Math.pow(2, n) * 6250l; i++) {

    }
  }

  @Measured
  public void slow(int n) {
    for (long i = 0; i < Math.pow(2, n) * 25000l; i++) {

    }
  }
}
