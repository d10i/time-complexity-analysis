package tech.dario.dissertation.timecomplexityanalysis.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactory;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactoryUtil;

public class TimeComplexityAnalysisSdk {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeComplexityAnalysisSdk.class);

  private final TimeRecorderFactory timeRecorderFactory;

  public TimeComplexityAnalysisSdk(TimeRecorderFactory timeRecorderFactory) {
    this.timeRecorderFactory = timeRecorderFactory;
    TimeRecorderFactoryUtil.setTimeRecorderFactory(timeRecorderFactory);
  }

  public void analyseAlgorithm(Algorithm algorithm) {
    long start;
    for (int i = 1; i <= 10; i++) {
      int n = i * i * 2;
      start = System.nanoTime();
      runAlgorithmWithN(algorithm, n);
      LOGGER.info("{}\t{}", n, String.format("%.4f", (System.nanoTime() - start) / 1000000000.0f));
    }
  }

  public void runAlgorithmWithN(Algorithm algorithm, int n) {
    LOGGER.info("runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
    timeRecorderFactory.start();
    algorithm.run(n);
    timeRecorderFactory.stop();
    LOGGER.info("Done runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
  }
}
