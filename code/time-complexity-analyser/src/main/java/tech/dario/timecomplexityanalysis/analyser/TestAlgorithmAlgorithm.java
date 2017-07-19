package tech.dario.timecomplexityanalysis.analyser;

import tech.dario.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.timecomplexityanalysis.testalgorithms.custom.TestAlgorithm;

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
  public Object run() {
    testAlgorithm.doTask(n);
    return 0;
  }
}
