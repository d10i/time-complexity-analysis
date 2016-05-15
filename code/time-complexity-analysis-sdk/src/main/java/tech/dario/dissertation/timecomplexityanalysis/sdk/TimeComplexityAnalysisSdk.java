package tech.dario.dissertation.timecomplexityanalysis.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.agent.annotations.Measured;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Algorithm;

public class TimeComplexityAnalysisSdk {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeComplexityAnalysisSdk.class);

  public void analyseAlgorithm(Algorithm algorithm) {
    long start;
    for (int i = 1; i <= 10; i++) {
      int n = i * i * 2;
      start = System.nanoTime();
      runAlgorithmWithN(algorithm, n);
      LOGGER.info("{}\t{}", n, String.format("%.4f", (System.nanoTime() - start) / 1000000000.0f));
    }
  }

  @Measured
  private void runAlgorithmWithN(Algorithm algorithm, int n) {
    LOGGER.info("runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
    algorithm.run(n);
    LOGGER.info("Done runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
  }
}
