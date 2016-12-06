package tech.dario.dissertation.timecomplexityanalysis.sdk.mappers;

import tech.dario.dissertation.timerecorder.tree.MergeableNode;
import tech.dario.dissertation.timerecorder.tree.MergeableTree;
import tech.dario.dissertation.timerecorder.tree.Metrics;

import java.util.function.Function;

public class TreeNormaliser implements Function<MergeableTree<Metrics>, MergeableTree<Metrics>> {
  @Override
  public MergeableTree<Metrics> apply(MergeableTree<Metrics> tree) {
    MergeableNode<Metrics> normalisedRootNode = tree.map(new NodeNormaliser());
    return new MergeableTree<>(normalisedRootNode);
  }

  private class NodeNormaliser implements Function<MergeableNode<Metrics>, MergeableNode<Metrics>> {
    @Override
    public MergeableNode<Metrics> apply(MergeableNode<Metrics> node) {
      if (node.getData() == null) {
        return new MergeableNode<>(node.getName(), null);
      }

      double total = node.getData().getTotal();

      for (MergeableNode<Metrics> child : node.getChildren().values()) {
        total -= child.getData().getTotal();
      }

      double countMultiplier = (node.getParent() != null && node.getParent().getData() != null) ? node.getParent().getData().getCount() : 1.0d;

      final Metrics newMetrics = new Metrics(node.getData().getCount() / countMultiplier, total / countMultiplier);
      return new MergeableNode<>(node.getName(), newMetrics);
    }
  }
}
