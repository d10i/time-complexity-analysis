package tech.dario.timecomplexityanalysis.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.sdk.domain.*;
import tech.dario.timecomplexityanalysis.sdk.mappers.*;
import tech.dario.timecomplexityanalysis.timerecorder.api.StaticTimeRecorderFactory;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.tree.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class TimeComplexityAnalysisSdk {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeComplexityAnalysisSdk.class);

  private static final DecimalFormat DECIMAL_FORMAT;
  static {
    DECIMAL_FORMAT = new DecimalFormat();
    DECIMAL_FORMAT.setMaximumFractionDigits(1);
    DECIMAL_FORMAT.setMinimumFractionDigits(1);
    DECIMAL_FORMAT.setGroupingSize(3);
    DECIMAL_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    DECIMAL_FORMAT.setDecimalSeparatorAlwaysShown(true);
    DECIMAL_FORMAT.setGroupingUsed(true);
  }

  public void analyseAlgorithm(final Algorithm algorithm) {
    analyseAlgorithm(algorithm, new DefaultProbe());
  }

  public void analyseAlgorithm(final Algorithm algorithm, final Probe probe) {
    long t0 = System.nanoTime();
    runWarmUpRounds(algorithm, probe.getNumWarmUpRounds(), probe.buildNIterator());

    final Map<Long, MergeableTree<Metrics>> treesGroupedByN = new HashMap<>();
    final Iterator<Long> iterator = probe.buildNIterator();
    while (iterator.hasNext()) {
      final long n = iterator.next();
      final List<MergeableTree<Metrics>> recordedTrees = runRecordingRounds(n, algorithm, probe.getNumRecordingRounds());
      final MergeableTree<MergeableList<Metrics>> sequencedTree = sequenceTrees(recordedTrees);
      System.out.println("Sequenced:\n" + sequencedTree);
      final MergeableTree<MergeableList<Metrics>> cleanedUpTree = cleanUpTree(sequencedTree, probe.getNumMaxOutliers());
      System.out.println("Cleaned up:\n" + cleanedUpTree);
      final MergeableTree<Metrics> averagedTree = averageTree(cleanedUpTree);
      System.out.println("Averaged:\n" + averagedTree);
      final MergeableTree<Metrics> normalisedTree = normaliseTree(averagedTree);
      System.out.println("Normalised:\n" + normalisedTree);
      treesGroupedByN.put(n, normalisedTree);
    }

    final MergeableTree<AggregatedMetrics> aggregatedTree = aggregateTrees(treesGroupedByN);
    System.out.println("Aggregated:\n" + aggregatedTree);
    final InterpolatedTree interpolatedTree = InterpolatedTree.fromAggregatedMetrics(aggregatedTree);
    System.out.println("Interpolated:\n" + interpolatedTree);
//    System.out.println("n = 1: " + toMicroSeconds(interpolatedTree.calculate(1)));
//    System.out.println("n = 2: " + toMicroSeconds(interpolatedTree.calculate(2)));
//    System.out.println("n = 4: " + toMicroSeconds(interpolatedTree.calculate(4)));
//    System.out.println("n = 8: " + toMicroSeconds(interpolatedTree.calculate(8)));
//    System.out.println("n = 16: " + toMicroSeconds(interpolatedTree.calculate(16)));
//    System.out.println("n = 32: " + toMicroSeconds(interpolatedTree.calculate(32)));
//    System.out.println("n = 64: " + toMicroSeconds(interpolatedTree.calculate(64)));
//    System.out.println("n = 128: " + toMicroSeconds(interpolatedTree.calculate(128)));
//    System.out.println("n = 256: " + toMicroSeconds(interpolatedTree.calculate(256)));
//    System.out.println("n = 512: " + toMicroSeconds(interpolatedTree.calculate(512)));
//    System.out.println("n = 1024: " + toMicroSeconds(interpolatedTree.calculate(1024)));
//    System.out.println("n = 2048: " + toMicroSeconds(interpolatedTree.calculate(2048)));
//    System.out.println("n = 4096: " + toMicroSeconds(interpolatedTree.calculate(4096)));
//    System.out.println("n = 8192: " + toMicroSeconds(interpolatedTree.calculate(8192)));
//    System.out.println("n = 16384: " + toMicroSeconds(interpolatedTree.calculate(16384)));
//    System.out.println("n = 32768: " + toMicroSeconds(interpolatedTree.calculate(32768)));
//    System.out.println("n = 65536: " + toMicroSeconds(interpolatedTree.calculate(65536)));
//    System.out.println("n = 131072: " + toMicroSeconds(interpolatedTree.calculate(131072)));
    System.out.println(toMicroSeconds(interpolatedTree.calculate(1)));
    System.out.println(toMicroSeconds(interpolatedTree.calculate(144)));
    System.out.println(toMicroSeconds(interpolatedTree.calculate(286)));
    System.out.println(toMicroSeconds(interpolatedTree.calculate(429)));
    System.out.println(toMicroSeconds(interpolatedTree.calculate(572)));
    System.out.println(toMicroSeconds(interpolatedTree.calculate(715)));
    System.out.println(toMicroSeconds(interpolatedTree.calculate(857)));
    System.out.println(toMicroSeconds(interpolatedTree.calculate(1000)));
    System.out.println("Took: " + toMicroSeconds(System.nanoTime() - t0));
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

  private MergeableTree<MergeableList<Metrics>> sequenceTrees(final List<MergeableTree<Metrics>> recordedTrees) {
    return recordedTrees
        .stream()
        .reduce(new MergeableTree<>(),
            (metricsListMergeableTree, metricsMergeableTree) -> {
              final MergeableTree<MergeableList<Metrics>> metricsMergeableTree2 = metricsMergeableTree.map(MergeableTree::new, new MetricsNodeSequencer<>());
              return metricsListMergeableTree.mergeWith(metricsMergeableTree2);
            }, MergeableTree::mergeWith
        );
  }

  private MergeableTree<MergeableList<Metrics>> cleanUpTree(final MergeableTree<MergeableList<Metrics>> sequencedTree, final int numMaxOutliers) {
    return sequencedTree.map(MergeableTree::new, new MetricsListNodeCleaner<>(numMaxOutliers));
  }

  private MergeableTree<Metrics> averageTree(final MergeableTree<MergeableList<Metrics>> cleanedUpTree) {
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

  private MergeableTree<Metrics> runAlgorithmWithN(final Algorithm algorithm, final long n) {
    try {
      LOGGER.info("runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
      algorithm.setup(n);
      final TimeRecorder timeRecorder = StaticTimeRecorderFactory.getTimeRecorder();
      timeRecorder.start();
      long t0 = System.nanoTime();
      algorithm.run();
      long t1 = System.nanoTime();
      System.out.println("n = " + n + ": " + toMicroSeconds(t1 - t0));
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

  private String toMicroSeconds(double nanoSeconds) {
    return DECIMAL_FORMAT.format(nanoSeconds / 1000) + " μs";
  }
}
