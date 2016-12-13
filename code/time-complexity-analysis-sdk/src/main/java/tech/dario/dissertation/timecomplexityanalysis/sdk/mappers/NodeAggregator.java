package tech.dario.dissertation.timecomplexityanalysis.sdk.mappers;

import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.dissertation.timerecorder.tree.AbstractNode;
import tech.dario.dissertation.timerecorder.tree.MergeableNode;
import tech.dario.dissertation.timerecorder.tree.Metrics;

import java.util.function.Function;

// TODO: better naming as this doesn't aggregate anything really...
public class NodeAggregator<T extends AbstractNode<Metrics, T>> implements Function<T, MergeableNode<AggregatedMetrics>> {

  private final long n;

  public NodeAggregator(long n) {
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
