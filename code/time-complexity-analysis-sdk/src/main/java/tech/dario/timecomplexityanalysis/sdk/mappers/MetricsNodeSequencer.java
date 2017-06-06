package tech.dario.timecomplexityanalysis.sdk.mappers;

import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Metrics;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableList;

import java.util.function.Function;

public class MetricsNodeSequencer<T extends AbstractNode<Metrics, T>> implements Function<T, MergeableNode<MergeableList<Metrics>>> {
  @Override
  public MergeableNode<MergeableList<Metrics>> apply(T node) {
    return new MergeableNode<>(node.getName(), node.getData() != null ? MergeableList.fromElement(node.getData()) : null);
  }
}
