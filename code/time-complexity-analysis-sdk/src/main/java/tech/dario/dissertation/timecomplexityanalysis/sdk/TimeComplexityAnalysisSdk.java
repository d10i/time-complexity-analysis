package tech.dario.dissertation.timecomplexityanalysis.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.dissertation.timecomplexityanalysis.sdk.iterator.ExponentialIterator;
import tech.dario.dissertation.timecomplexityanalysis.sdk.mappers.MetricsAggregator;
import tech.dario.dissertation.timecomplexityanalysis.sdk.mappers.TreeNormaliser;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactory;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactoryUtil;
import tech.dario.dissertation.timerecorder.tree.Metrics;
import tech.dario.dissertation.timerecorder.tree.MergeableTree;

import java.util.*;
import java.util.stream.Collectors;

public class TimeComplexityAnalysisSdk {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeComplexityAnalysisSdk.class);

  private final TimeRecorderFactory timeRecorderFactory;

  public TimeComplexityAnalysisSdk(TimeRecorderFactory timeRecorderFactory) {
    this.timeRecorderFactory = timeRecorderFactory;
    TimeRecorderFactoryUtil.setTimeRecorderFactory(timeRecorderFactory);
  }

  public void analyseAlgorithm(Algorithm algorithm) {
    Map<Long, MergeableTree<Metrics>> trees = new HashMap<>();

    Iterator<Long> iterator = new ExponentialIterator(5, 6);

    while (iterator.hasNext()) {
      long n = iterator.next();
      trees.put(n, runAlgorithmWithN(algorithm, n));
    }

    trees = normaliseTrees(trees);

    MergeableTree<AggregatedMetrics> lol = analyseTrees(trees);
    System.out.println(lol);
  }

  public MergeableTree<Metrics> runAlgorithmWithN(Algorithm algorithm, long n) {
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

  private Map<Long, MergeableTree<Metrics>> normaliseTrees(Map<Long, MergeableTree<Metrics>> trees) {
    Map<Long, MergeableTree<Metrics>> newTrees = new HashMap<>();
    for (Map.Entry<Long, MergeableTree<Metrics>> treeEntry : trees.entrySet()) {
      newTrees.put(treeEntry.getKey(), treeEntry.getValue().flatMap(new TreeNormaliser()));
    }

    return newTrees;
  }

  private MergeableTree<AggregatedMetrics> analyseTrees(Map<Long, MergeableTree<Metrics>> trees) {
    MergeableTree<AggregatedMetrics> result = null;
    for (Map.Entry<Long, MergeableTree<Metrics>> treeEntry : trees.entrySet()) {
      final long n = treeEntry.getKey();
      final MergeableTree<Metrics> metricsTree = treeEntry.getValue();

      result = metricsTree.flatMap(new MetricsAggregator(n)).mergeWith(result);
    }

    return result;
  }
}
