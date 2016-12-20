package tech.dario.dissertation.testalgorithm;

import tech.dario.dissertation.agent.annotations.Measured;

public class Cubic {
  // quick(1000) ~ 1ms ~ 3000000 iterations
  @Measured
  public void quick(long n) {
    double v = Math.pow(n, 3) * 0.003d;
    for (long i = 0; i < v; i++) {

    }
  }

  // average(1000) ~ 10ms ~ 30000000 iterations
  @Measured
  public void average(long n) {
    double v = Math.pow(n, 3) * 0.03d;
    for (long i = 0; i < v; i++) {

    }
  }

  // slow(1000) ~ 100ms ~ 300000000 iterations
  @Measured
  public void slow(long n) {
    double v = Math.pow(n, 3) * 0.3d;
    for (long i = 0; i < v; i++) {

    }
  }
}
