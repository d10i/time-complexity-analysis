package tech.dario.timecomplexityanalysis.sdk;

import java.lang.ref.WeakReference;
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

  public InterpolatedTree analyseAlgorithm(final Algorithm algorithm) {
    return analyseAlgorithm(algorithm, new DefaultProbe());
  }

  public InterpolatedTree analyseAlgorithm(final Algorithm algorithm, final Probe probe) {
    long t0 = System.nanoTime();
    runWarmUpRounds(algorithm, probe);

    final Map<Long, MergeableTree<Measurement>> treesGroupedByN = new HashMap<>();
    final Iterator<Long> iterator = probe.buildNIterator();
    while (iterator.hasNext()) {
      final long n = iterator.next();
      final Collection<MergeableTree<Measurement>> recordedTrees = runRecordingRounds(n, algorithm, probe.getNumRecordingRounds());
      final MergeableTree<MergeableCollection<Measurement>> sequencedTree = sequenceTrees(recordedTrees);
      LOGGER.debug("Sequenced:\n" + sequencedTree);
      final MergeableTree<MergeableCollection<Measurement>> cleanedUpTree = cleanUpTree(sequencedTree, probe.getNumMaxOutliers());
      LOGGER.debug("Cleaned up:\n" + cleanedUpTree);
      final MergeableTree<Measurement> averagedTree = averageTree(cleanedUpTree);
      LOGGER.debug("Averaged:\n" + averagedTree);
      printTotalTime(n, averagedTree);
      final MergeableTree<Measurement> normalisedTree = normaliseTree(averagedTree);
      LOGGER.debug("Normalised:\n" + normalisedTree);
      treesGroupedByN.put(n, normalisedTree);
    }

    final MergeableTree<AggregatedMeasurement> aggregatedTree = aggregateTrees(treesGroupedByN);
    LOGGER.debug("Aggregated:\n" + aggregatedTree);
    final InterpolatedTree interpolatedTree = InterpolatedTree.fromAggregatedMeasurement(aggregatedTree, probe);
    LOGGER.debug("Interpolated:\n" + interpolatedTree);
    LOGGER.debug("Took: " + (System.nanoTime() - t0) + " ns");
    return interpolatedTree;
  }

  private void printTotalTime(long n, MergeableTree<Measurement> averagedTree) {
    try {
      double totalTime = averagedTree.getRootNode().getChild("tech.dario.timecomplexityanalysis.testalgorithm.TestAlgorithm.doTask(long)").getData().getTotal();
      LOGGER.debug(String.format("n=%d;%.1f", n, totalTime / 1000));
    } catch (NullPointerException ignored) {

    }
  }

  private void runWarmUpRounds(final Algorithm algorithm, final Probe probe) {
    for (int i = 0; i < probe.getNumWarmUpRounds(); i++) {
      Iterator<Long> nIterator = probe.buildNIterator();
      while (nIterator.hasNext()) {
        long n = nIterator.next();
        runAlgorithmWithN(algorithm, n);
      }
    }
  }

  private Collection<MergeableTree<Measurement>> runRecordingRounds(final long n, final Algorithm algorithm, final int numRecordingRounds) {
    Collection<MergeableTree<Measurement>> trees = new ArrayList<>(numRecordingRounds);

    for (int i = 0; i < numRecordingRounds; i++) {
      trees.add(runAlgorithmWithN(algorithm, n));
    }

    return trees;
  }

  private MergeableTree<MergeableCollection<Measurement>> sequenceTrees(final Collection<MergeableTree<Measurement>> recordedTrees) {
    return recordedTrees
        .stream()
        .reduce(new MergeableTree<>(),
            (measurementCollectionMergeableTree, measurementMergeableTree) -> {
              final MergeableTree<MergeableCollection<Measurement>> measurementMergeableTree2 = measurementMergeableTree.map(MergeableTree::new, new MeasurementNodeSequencer<>());
              return measurementCollectionMergeableTree.mergeWith(measurementMergeableTree2);
            }, MergeableTree::mergeWith
        );
  }

  private MergeableTree<MergeableCollection<Measurement>> cleanUpTree(final MergeableTree<MergeableCollection<Measurement>> sequencedTree, final int numMaxOutliers) {
    return sequencedTree.map(MergeableTree::new, new MeasurementCollectionNodeCleaner<>(numMaxOutliers));
  }

  private MergeableTree<Measurement> averageTree(final MergeableTree<MergeableCollection<Measurement>> cleanedUpTree) {
    return cleanedUpTree.map(MergeableTree::new, new MeasurementCollectionNodeAverager<>());
  }

  private MergeableTree<Measurement> normaliseTree(final MergeableTree<Measurement> averagedTree) {
    return averagedTree.map(MergeableTree::new, new MeasurementNodeNormaliser<>());
  }

  private MergeableTree<AggregatedMeasurement> aggregateTrees(final Map<Long, MergeableTree<Measurement>> normalisedTreeMap) {
    MergeableTree<AggregatedMeasurement> result = null;
    for (Map.Entry<Long, MergeableTree<Measurement>> normalisedTreeEntry : normalisedTreeMap.entrySet()) {
      final long n = normalisedTreeEntry.getKey();
      final MergeableTree<Measurement> normalisedTree = normalisedTreeEntry.getValue();
      result = normalisedTree.map(MergeableTree::new, new MeasurementNodeAggregator<>(n)).mergeWith(result);
    }

    return result;
  }

  private MergeableTree<Measurement> runAlgorithmWithN(final Algorithm algorithm, final long n) {
    try {
      LOGGER.info("runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
      algorithm.setup(n);
      final TimeRecorder timeRecorder = StaticTimeRecorderFactory.getTimeRecorder();
      timeRecorder.start();
      //forceGarbageCollection();
      long t0 = System.nanoTime();
      Object returnedValue = algorithm.run();
      long t1 = System.nanoTime();
      LOGGER.debug("n = " + n + ": " + (t1 - t0));
      MergeableTree<Measurement> tree = timeRecorder.stop();
      LOGGER.info("Done runAlgorithmWithN: algorithm: {}, n: {}", algorithm, n);
      return tree;
      //return new MergeableTree<>();
    } catch (Exception e) {
      String message = String.format("Unexpected error analysing algorithm with n %d", n);
      LOGGER.error(message, e);
      System.exit(1);
      return null;
    }
  }

  private void forceGarbageCollection() {
    Object obj = new Object();
    WeakReference ref = new WeakReference<>(obj);
    obj = null;
    while (ref.get() != null) {
      System.gc();
    }
  }
}
