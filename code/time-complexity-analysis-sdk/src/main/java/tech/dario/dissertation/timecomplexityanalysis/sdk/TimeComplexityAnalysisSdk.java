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
    for (int i = 1; i <= 4; i++) {
    // for (int i = 1; i <= 2; i++) {
    // for (int i = 1; i <= 1; i++) {
      int n = i * i * 2;
      runAlgorithmWithN(algorithm, n);
    }
  }

  public void runAlgorithmWithN(Algorithm algorithm, int n) {
    LOGGER.info("runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
    long t0 = System.nanoTime();
    timeRecorderFactory.start();
    long t1 = System.nanoTime();
    algorithm.run(n);
    long t2 = System.nanoTime();
    timeRecorderFactory.stop();
    long t3 = System.nanoTime();
    LOGGER.info("Done runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
    LOGGER.info("Start time: {}", String.format("%.4f", (t1 - t0) / 1000000000.0f));
    LOGGER.info("Algorithm time: {}", String.format("%.4f", (t2 - t1) / 1000000000.0f));
    LOGGER.info("Stop time: {}", String.format("%.4f", (t3 - t2) / 1000000000.0f));
  }
}
