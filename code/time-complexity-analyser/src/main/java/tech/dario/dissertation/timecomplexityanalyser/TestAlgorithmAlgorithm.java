package tech.dario.dissertation.timecomplexityanalyser;

import tech.dario.dissertation.testalgorithm.TestAlgorithm;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Algorithm;

public class TestAlgorithmAlgorithm implements Algorithm {

  private final TestAlgorithm testAlgorithm;

  public TestAlgorithmAlgorithm(TestAlgorithm testAlgorithm) {
    this.testAlgorithm = testAlgorithm;
  }

  @Override
  public void run(long n) {
    this.testAlgorithm.doTask(n);
  }
}
