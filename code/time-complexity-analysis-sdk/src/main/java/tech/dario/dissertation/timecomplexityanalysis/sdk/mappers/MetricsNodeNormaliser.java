package tech.dario.dissertation.timecomplexityanalysis.sdk.mappers;

import tech.dario.dissertation.timerecorder.tree.AbstractNode;
import tech.dario.dissertation.timerecorder.tree.MergeableNode;
import tech.dario.dissertation.timerecorder.tree.Metrics;

import java.util.function.Function;

public class MetricsNodeNormaliser<T extends AbstractNode<Metrics, T>> implements Function<T, MergeableNode<Metrics>> {
  @Override
  public MergeableNode<Metrics> apply(T node) {
    if (node.getData() == null) {
      return new MergeableNode<>(node.getName(), null);
    }

    double total = node.getData().getTotal();

    for (T child : node.getChildren().values()) {
      total -= child.getData().getTotal();
    }

    double countMultiplier = (node.getParent() != null && node.getParent().getData() != null) ? node.getParent().getData().getCount() : 1.0d;

    final Metrics newMetrics = new Metrics(node.getData().getCount() / countMultiplier, total / countMultiplier);
    return new MergeableNode<>(node.getName(), newMetrics);
  }
}
