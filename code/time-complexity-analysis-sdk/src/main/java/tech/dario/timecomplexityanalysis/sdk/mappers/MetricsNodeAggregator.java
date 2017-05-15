package tech.dario.timecomplexityanalysis.sdk.mappers;

import tech.dario.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Metrics;

import java.util.function.Function;

public class MetricsNodeAggregator<T extends AbstractNode<Metrics, T>> implements Function<T, MergeableNode<AggregatedMetrics>> {

  private final long n;

  public MetricsNodeAggregator(long n) {
    this.n = n;
  }

  @Override
  public MergeableNode<AggregatedMetrics> apply(T node) {
    if (node.getData() == null) {
      return new MergeableNode<>(node.getName(), null);
    }

    return new MergeableNode<>(node.getName(), new AggregatedMetrics(n, node.getData()));
  }
}
