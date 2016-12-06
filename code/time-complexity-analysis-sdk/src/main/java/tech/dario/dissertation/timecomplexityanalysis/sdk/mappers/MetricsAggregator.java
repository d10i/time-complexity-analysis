package tech.dario.dissertation.timecomplexityanalysis.sdk.mappers;

import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.dissertation.timerecorder.tree.MergeableNode;
import tech.dario.dissertation.timerecorder.tree.MergeableTree;
import tech.dario.dissertation.timerecorder.tree.Metrics;

import java.util.function.Function;

// TODO: better naming as this doesn't aggregate anything really...
public class MetricsAggregator implements Function<MergeableTree<Metrics>, MergeableTree<AggregatedMetrics>> {

  private final long n;

  public MetricsAggregator(long n) {
    this.n = n;
  }

  @Override
  public MergeableTree<AggregatedMetrics> apply(MergeableTree<Metrics> tree) {
    MergeableNode<AggregatedMetrics> aggregatedRootNode = tree.map(new NodeAggregator());
    return new MergeableTree<>(aggregatedRootNode);
  }

  private class NodeAggregator implements Function<MergeableNode<Metrics>, MergeableNode<AggregatedMetrics>> {
    @Override
    public MergeableNode<AggregatedMetrics> apply(MergeableNode<Metrics> node) {
      return new MergeableNode<>(node.getName(), new AggregatedMetrics(n, node.getData()));
    }
  }
}
