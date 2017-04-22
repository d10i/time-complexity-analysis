package tech.dario.timecomplexityanalysis.analyser;

import tech.dario.timecomplexityanalysis.testalgorithm.TestAlgorithm;
import tech.dario.timecomplexityanalysis.sdk.domain.Algorithm;

public class TestAlgorithmAlgorithm implements Algorithm {

  private final TestAlgorithm testAlgorithm;
  private long n;

  public TestAlgorithmAlgorithm() {
    this.testAlgorithm = new TestAlgorithm();
  }

  @Override
  public void setup(long n) {
    this.n = n;
  }

  @Override
  public void run() {
    this.testAlgorithm.doTask(n);
  }
}
