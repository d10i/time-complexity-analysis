package tech.dario.dissertation.timecomplexityanalysis.sdk.mappers;

import org.apache.commons.math3.distribution.TDistribution;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Probe;
import tech.dario.dissertation.timerecorder.tree.AbstractNode;
import tech.dario.dissertation.timerecorder.tree.MergeableNode;
import tech.dario.dissertation.timerecorder.tree.Metrics;
import tech.dario.dissertation.timerecorder.tree.MetricsList;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO: better naming as this doesn't aggregate anything really...
public class MetricsListNodeAggregator<T extends AbstractNode<MetricsList, T>> implements Function<T, MergeableNode<AggregatedMetrics>> {

  private final long n;
  private final Probe probe;

  public MetricsListNodeAggregator(long n, Probe probe) {
    this.n = n;
    this.probe = probe;
  }

  @Override
  public MergeableNode<AggregatedMetrics> apply(T node) {
    if (node.getData() == null) {
      return new MergeableNode<>(node.getName(), null);
    }

    final List<Metrics> metricsList = node.getData().getList();
    final List<Metrics> metricsListWithoutOutliers = removeOutliers(metricsList);
    final Metrics averageMetrics = computeAverageMetrics(metricsListWithoutOutliers);

    return new MergeableNode<>(node.getName(), new AggregatedMetrics(n, averageMetrics));
  }

  private List<Metrics> removeOutliers(List<Metrics> metricsList) {
    // Using generalized extreme Studentized deviate (ESD)
    // See: http://www.itl.nist.gov/div898/handbook/eda/section3/eda35h3.htm
    double[] xs = metricsList.stream().map(Metrics::getTotal).mapToDouble(m -> m).toArray();
    if(xs.length <= probe.getNumMaxOutliers()) {
      return metricsList;
    }

    double alpha = 0.05d;

    double validRangeMin = min(xs);
    double validRangeMax = max(xs);

    for (int i = 1; i <= probe.getNumMaxOutliers(); i++) {
      double avg = avg(xs);
      double stdev = stdev(avg, xs);
      int mostOutlyingIndex = mostOutlyingIndex(avg, xs);
      double mostOutlying = xs[mostOutlyingIndex];

      double R = Math.abs(mostOutlying - avg) / stdev;

      double p = 1 - (alpha / (2 * xs.length));
      double tpv = tDistributionInverseCumulativeProbability(p, xs.length - 2);
      double lambda = (xs.length - 1) * tpv / Math.sqrt((xs.length - 2 + Math.pow(tpv, 2)) * xs.length);

      xs = removeElement(xs, mostOutlyingIndex);

      if (R > lambda) {
        validRangeMin = min(xs);
        validRangeMax = max(xs);
      }
    }

    final double finalValidRangeMin = validRangeMin;
    final double finalValidRangeMax = validRangeMax;

    return metricsList
            .stream()
            .filter(
                    m -> m.getTotal() >= finalValidRangeMin && m.getTotal() <= finalValidRangeMax
            )
            .collect(Collectors.toList());
  }

  private double avg(double[] xs) {
    double sum = 0.0d;
    for (double x : xs) {
      sum += x;
    }

    return sum / xs.length;
  }

  private double min(double[] xs) {
    double currentMin = Double.POSITIVE_INFINITY;
    for (double x : xs) {
      currentMin = Math.min(currentMin, x);
    }

    return currentMin;
  }

  private double max(double[] xs) {
    double currentMax = Double.NEGATIVE_INFINITY;
    for (double x : xs) {
      currentMax = Math.max(currentMax, x);
    }

    return currentMax;
  }

  private double stdev(double avg, double[] xs) {
    double sum = 0.0d;
    for (double x : xs) {
      sum += Math.pow(x - avg, 2);
    }

    return Math.sqrt(sum / (xs.length - 1));
  }

  private double tDistributionInverseCumulativeProbability(double p, int v) {
    return new TDistribution(v).inverseCumulativeProbability(p);
  }

  private double[] removeElement(double[] original, int i) {
    double[] n = new double[original.length - 1];
    System.arraycopy(original, 0, n, 0, i);
    System.arraycopy(original, i + 1, n, i, original.length - i - 1);
    return n;
  }

  private int mostOutlyingIndex(double avg, double[] xs) {
    double mostOutlying = Double.NEGATIVE_INFINITY;
    int mostOutlyingIndex = -1;
    for (int i = 0; i < xs.length; i++) {
      double diff = Math.abs(xs[i] - avg);
      if (diff > mostOutlying) {
        mostOutlying = diff;
        mostOutlyingIndex = i;
      }
    }

    return mostOutlyingIndex;
  }

  private Metrics computeAverageMetrics(List<Metrics> metricsList) {
    final int numMetrics = metricsList.size();
    double countSum = 0.0d;
    double totalSum = 0.0d;
    for (Metrics metrics : metricsList) {
      countSum += metrics.getCount();
      totalSum += metrics.getTotal();
    }

    return new Metrics(countSum / numMetrics, totalSum / numMetrics);
  }
}
