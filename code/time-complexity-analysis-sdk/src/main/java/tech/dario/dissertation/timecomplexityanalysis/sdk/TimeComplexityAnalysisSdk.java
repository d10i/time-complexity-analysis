package tech.dario.dissertation.timecomplexityanalysis.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactory;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactoryUtil;
import tech.dario.dissertation.timerecorder.tree.MetricsTree;

public class TimeComplexityAnalysisSdk {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeComplexityAnalysisSdk.class);

  private final TimeRecorderFactory timeRecorderFactory;

  public TimeComplexityAnalysisSdk(TimeRecorderFactory timeRecorderFactory) {
    this.timeRecorderFactory = timeRecorderFactory;
    TimeRecorderFactoryUtil.setTimeRecorderFactory(timeRecorderFactory);
  }

  public void analyseAlgorithm(Algorithm algorithm) {

//    Map<Integer, MetricsTree> trees = new HashMap<>();
//
//    for (int i = 1; i <= 4; i++) {
//    // for (int i = 1; i <= 2; i++) {
//    // for (int i = 1; i <= 1; i++) {
//      int n = i * i * 2;
//      trees.put(n, runAlgorithmWithN(algorithm, n));
//    }
  }

  public MetricsTree runAlgorithmWithN(Algorithm algorithm, int n) {
    try {
      LOGGER.info("runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
      long t0 = System.nanoTime();
      timeRecorderFactory.start();
      long t1 = System.nanoTime();
      algorithm.run(n);
      long t2 = System.nanoTime();
      MetricsTree tree = timeRecorderFactory.stop();
      long t3 = System.nanoTime();
      LOGGER.info("Done runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
      LOGGER.debug("Start time: {}", String.format("%.4f", (t1 - t0) / 1000000000.0f));
      LOGGER.debug("Algorithm time: {}", String.format("%.4f", (t2 - t1) / 1000000000.0f));
      LOGGER.debug("Stop time: {}", String.format("%.4f", (t3 - t2) / 1000000000.0f));
      return tree;
    } catch(Exception e) {
      String message = String.format("Unexpected error analysing algorithm with n %d", n);
      LOGGER.error(message, e);
      System.exit(1);
      return null;
    }
  }
}
