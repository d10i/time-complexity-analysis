package tech.dario.dissertation.timecomplexityanalysis.sdk.mappers;

import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.dissertation.timerecorder.tree.MergeableNode;
import tech.dario.dissertation.timerecorder.tree.Metrics;

import java.util.function.Function;

public class MetricsAggregator implements Function<MergeableNode<Metrics>, MergeableNode<AggregatedMetrics>> {

  private final long n;

  public MetricsAggregator(long n) {
    this.n = n;
  }

  @Override
  public MergeableNode<AggregatedMetrics> apply(MergeableNode<Metrics> node) {
    return new MergeableNode<>(node.getName(), new AggregatedMetrics(n, node.getData()));
  }
}
