package tech.dario.timecomplexityanalysis.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.timecomplexityanalysis.sdk.domain.DefaultProbe;
import tech.dario.timecomplexityanalysis.sdk.domain.Probe;
import tech.dario.timecomplexityanalysis.sdk.fitting.FittingFunction;
import tech.dario.timecomplexityanalysis.sdk.mappers.*;
import tech.dario.timecomplexityanalysis.timerecorder.api.StaticTimeRecorderFactory;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Metrics;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MetricsList;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleTree;

import java.util.*;

public class TimeComplexityAnalysisSdk {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeComplexityAnalysisSdk.class);

  public void analyseAlgorithm(final Algorithm algorithm) {
    analyseAlgorithm(algorithm, new DefaultProbe());
  }

  public void analyseAlgorithm(final Algorithm algorithm, final Probe probe) {
    runWarmUpRounds(algorithm, probe.getNumWarmUpRounds(), probe.buildNIterator());

    final Map<Long, MergeableTree<Metrics>> treesGroupedByN = new HashMap<>();
    final Iterator<Long> iterator = probe.buildNIterator();
    while (iterator.hasNext()) {
      final long n = iterator.next();
      final List<MergeableTree<Metrics>> recordedTrees = runRecordingRounds(n, algorithm, probe.getNumRecordingRounds());
      final MergeableTree<MetricsList> sequencedTree = sequenceTrees(recordedTrees);
      System.out.println("Sequenced:\n" + sequencedTree);
      final MergeableTree<MetricsList> cleanedUpTree = cleanUpTree(sequencedTree, probe.getNumMaxOutliers());
      System.out.println("Cleaned up:\n" + cleanedUpTree);
      final MergeableTree<Metrics> averagedTree = averageTree(cleanedUpTree);
      System.out.println("Averaged:\n" + averagedTree);
      final MergeableTree<Metrics> normalisedTree = normaliseTree(averagedTree);
      System.out.println("Normalised:\n" + normalisedTree);
      treesGroupedByN.put(n, normalisedTree);
    }

    final MergeableTree<AggregatedMetrics> aggregatedTree = aggregateTrees(treesGroupedByN);
    System.out.println("Aggregated:\n" + aggregatedTree);
    final SimpleTree<FittingFunction> interpolatedTree = interpolateTree(aggregatedTree);
    System.out.println("Interpolated:\n" + interpolatedTree);
  }

  private void runWarmUpRounds(final Algorithm algorithm, final int numWarmUpRounds, final Iterator<Long> nIterator) {
    for (int i = 0; i < numWarmUpRounds; i++) {
      while (nIterator.hasNext()) {
        long n = nIterator.next();
        runAlgorithmWithN(algorithm, n);
      }
    }
  }

  private List<MergeableTree<Metrics>> runRecordingRounds(final long n, final Algorithm algorithm, final int numRecordingRounds) {
    List<MergeableTree<Metrics>> trees = new ArrayList<>(numRecordingRounds);

    for (int i = 0; i < numRecordingRounds; i++) {
      trees.add(runAlgorithmWithN(algorithm, n));
    }

    return trees;
  }

  private MergeableTree<MetricsList> sequenceTrees(final List<MergeableTree<Metrics>> recordedTrees) {
    return recordedTrees
        .stream()
        .reduce(new MergeableTree<>(),
            (metricsListMergeableTree, metricsMergeableTree) -> {
              final MergeableTree<MetricsList> metricsMergeableTree2 = metricsMergeableTree.map(MergeableTree::new, new MetricsNodeSequencer<>());
              return metricsListMergeableTree.mergeWith(metricsMergeableTree2);
            }, MergeableTree::mergeWith
        );
  }

  private MergeableTree<MetricsList> cleanUpTree(final MergeableTree<MetricsList> sequencedTree, final int numMaxOutliers) {
    return sequencedTree.map(MergeableTree::new, new MetricsListNodeCleaner<>(numMaxOutliers));
  }

  private MergeableTree<Metrics> averageTree(final MergeableTree<MetricsList> cleanedUpTree) {
    return cleanedUpTree.map(MergeableTree::new, new MetricsListNodeAverager<>());
  }

  private MergeableTree<Metrics> normaliseTree(final MergeableTree<Metrics> averagedTree) {
    return averagedTree.map(MergeableTree::new, new MetricsNodeNormaliser<>());
  }

  private MergeableTree<AggregatedMetrics> aggregateTrees(final Map<Long, MergeableTree<Metrics>> normalisedTreeMap) {
    MergeableTree<AggregatedMetrics> result = null;
    for (Map.Entry<Long, MergeableTree<Metrics>> normalisedTreeEntry : normalisedTreeMap.entrySet()) {
      final long n = normalisedTreeEntry.getKey();
      final MergeableTree<Metrics> normalisedTree = normalisedTreeEntry.getValue();
      result = normalisedTree.map(MergeableTree::new, new MetricsNodeAggregator<>(n)).mergeWith(result);
    }

    return result;
  }

  private SimpleTree<FittingFunction> interpolateTree(final MergeableTree<AggregatedMetrics> aggregatedTree) {
    return aggregatedTree.map(SimpleTree::new, new AggregatedMetricsNodeAnalyser<>());
  }

  private MergeableTree<Metrics> runAlgorithmWithN(final Algorithm algorithm, final long n) {
    try {
      LOGGER.info("runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
      algorithm.setup(n);
      final TimeRecorder timeRecorder = StaticTimeRecorderFactory.getTimeRecorder();
      timeRecorder.start();
      algorithm.run();
      MergeableTree<Metrics> tree = timeRecorder.stop();
      LOGGER.info("Done runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
      return tree;
    } catch (Exception e) {
      String message = String.format("Unexpected error analysing algorithm with n %d", n);
      LOGGER.error(message, e);
      System.exit(1);
      return null;
    }
  }
}
