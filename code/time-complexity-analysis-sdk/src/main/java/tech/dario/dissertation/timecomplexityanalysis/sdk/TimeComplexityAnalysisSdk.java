package tech.dario.dissertation.timecomplexityanalysis.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.DefaultProbe;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Probe;
import tech.dario.dissertation.timecomplexityanalysis.sdk.fitting.FittingFunction;
import tech.dario.dissertation.timecomplexityanalysis.sdk.mappers.Asd;
import tech.dario.dissertation.timecomplexityanalysis.sdk.mappers.MetricsListNodeAggregator;
import tech.dario.dissertation.timecomplexityanalysis.sdk.mappers.AggregatedMetricsNodeAnalyser;
import tech.dario.dissertation.timecomplexityanalysis.sdk.mappers.MetricsNodeNormaliser;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactory;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactoryUtil;
import tech.dario.dissertation.timerecorder.tree.*;

import java.util.*;

public class TimeComplexityAnalysisSdk {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeComplexityAnalysisSdk.class);

  private final TimeRecorderFactory timeRecorderFactory;

  public TimeComplexityAnalysisSdk(TimeRecorderFactory timeRecorderFactory) {
    this.timeRecorderFactory = timeRecorderFactory;
    TimeRecorderFactoryUtil.setTimeRecorderFactory(timeRecorderFactory);
  }

  public void analyseAlgorithm(Algorithm algorithm) {
    analyseAlgorithm(algorithm, new DefaultProbe());
  }

  public void analyseAlgorithm(Algorithm algorithm, Probe probe) {

    runWarmUpRounds(algorithm, probe);

    Map<Long, MergeableTree<MetricsList>> trees = runRecordingRounds(algorithm, probe);

    MergeableTree<AggregatedMetrics> lol = aggregateTrees(trees, probe);
    System.out.println(lol);
    SimpleTree<FittingFunction> asd = lol.map(SimpleTree::new, new AggregatedMetricsNodeAnalyser<>());
    System.out.println(asd);
  }

  private void runWarmUpRounds(Algorithm algorithm, Probe probe) {
    for(int i = 0; i < probe.getNumWarmUpRounds(); i++) {
      final Iterator<Long> iterator = probe.buildNIterator();
      while (iterator.hasNext()) {
        long n = iterator.next();
        runAlgorithmWithN(algorithm, n);
      }
    }
  }

  private Map<Long, MergeableTree<MetricsList>> runRecordingRounds(Algorithm algorithm, Probe probe) {
    Map<Long, MergeableTree<MetricsList>> trees = new HashMap<>();
    for(int i = 0; i < probe.getNumRecordingRounds(); i++) {
      final Iterator<Long> iterator = probe.buildNIterator();
      while (iterator.hasNext()) {
        final long n = iterator.next();

        final MergeableTree<Metrics> metricsTree = runAlgorithmWithN(algorithm, n);
        final MergeableTree<Metrics> normalisedMetricsTree = metricsTree.map(MergeableTree::new, new MetricsNodeNormaliser<>());
        final MergeableTree<MetricsList> metricsListTree = normalisedMetricsTree.map(MergeableTree::new, new Asd<>());

        if(trees.containsKey(n)) {
          trees.put(n, trees.get(n).mergeWith(metricsListTree));
        } else {
          trees.put(n, metricsListTree);
        }
      }
    }

    return trees;
  }

  private MergeableTree<Metrics> runAlgorithmWithN(Algorithm algorithm, long n) {
    try {
      LOGGER.info("runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
      long t0 = System.nanoTime();
      timeRecorderFactory.start();
      long t1 = System.nanoTime();
      algorithm.run(n);
      long t2 = System.nanoTime();
      MergeableTree<Metrics> tree = timeRecorderFactory.stop();
      long t3 = System.nanoTime();
      LOGGER.info("Done runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
      LOGGER.debug("Start time: {}", String.format("%.4f", (t1 - t0) / 1000000000.0f));
      LOGGER.debug("Algorithm time: {}", String.format("%.4f", (t2 - t1) / 1000000000.0f));
      LOGGER.debug("Stop time: {}", String.format("%.4f", (t3 - t2) / 1000000000.0f));
      return tree;
    } catch (Exception e) {
      String message = String.format("Unexpected error analysing algorithm with n %d", n);
      LOGGER.error(message, e);
      System.exit(1);
      return null;
    }
  }

  private MergeableTree<AggregatedMetrics> aggregateTrees(Map<Long, MergeableTree<MetricsList>> trees, Probe probe) {
    MergeableTree<AggregatedMetrics> result = null;
    for (Map.Entry<Long, MergeableTree<MetricsList>> treeEntry : trees.entrySet()) {
      final long n = treeEntry.getKey();
      result = treeEntry.getValue().map(MergeableTree::new, new MetricsListNodeAggregator<>(n, probe)).mergeWith(result);
    }

    return result;
  }
}
