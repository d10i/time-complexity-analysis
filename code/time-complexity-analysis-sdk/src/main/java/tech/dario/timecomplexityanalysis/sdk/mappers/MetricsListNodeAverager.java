package tech.dario.timecomplexityanalysis.sdk.mappers;

import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Metrics;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MetricsList;

import java.util.List;
import java.util.function.Function;

public class MetricsListNodeAverager<T extends AbstractNode<MetricsList, T>> implements Function<T, MergeableNode<Metrics>> {
  @Override
  public MergeableNode<Metrics> apply(T node) {
    if (node.getData() == null) {
      return new MergeableNode<>(node.getName(), null);
    }

    final List<Metrics> metricsList = node.getData().getList();
    final Metrics averageMetrics = computeAverageMetrics(metricsList);
    return new MergeableNode<>(node.getName(), averageMetrics);
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
