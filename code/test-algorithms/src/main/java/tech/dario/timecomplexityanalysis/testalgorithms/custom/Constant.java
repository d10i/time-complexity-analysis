package tech.dario.timecomplexityanalysis.testalgorithms.custom;

public class Constant {
  // quick(1000) ~ 1ms ~ 3000000 iterations
  public void quick(long n) {
    for (long i = 0; i < 3000000L; i++) {

    }
  }

  // average(1000) ~ 10ms ~ 30000000 iterations
  public void average(long n) {
    for (long i = 0; i < 30000000L; i++) {

    }
  }

  // slow(1000) ~ 100ms ~ 300000000 iterations
  public void slow(long n) {
    for (long i = 0; i < 300000000L; i++) {

    }
  }
}
