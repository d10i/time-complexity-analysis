package tech.dario.timecomplexityanalysis.sdk.mappers;

import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;

import java.util.function.Function;

public class MeasurementNodeNormaliser<T extends AbstractNode<Measurement, T>> implements Function<T, MergeableNode<Measurement>> {
  @Override
  public MergeableNode<Measurement> apply(T node) {
    if (node.getData() == null) {
      return new MergeableNode<>(node.getName(), null);
    }

    double total = node.getData().getTotal();

    for (T child : node.getChildren().values()) {
      total -= child.getData().getTotal();
    }

    double countMultiplier = (node.getParent() != null && node.getParent().getData() != null) ? node.getParent().getData().getCount() : 1.0d;

    final Measurement newMeasurement = new Measurement(node.getData().getCount() / countMultiplier, total / countMultiplier);
    return new MergeableNode<>(node.getName(), newMeasurement);
  }
}
