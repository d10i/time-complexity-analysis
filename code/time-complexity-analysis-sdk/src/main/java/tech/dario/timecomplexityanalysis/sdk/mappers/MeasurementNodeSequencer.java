package tech.dario.timecomplexityanalysis.sdk.mappers;

import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableList;

import java.util.function.Function;

public class MeasurementNodeSequencer<T extends AbstractNode<Measurement, T>> implements Function<T, MergeableNode<MergeableList<Measurement>>> {
  @Override
  public MergeableNode<MergeableList<Measurement>> apply(T node) {
    return new MergeableNode<>(node.getName(), node.getData() != null ? MergeableList.fromElement(node.getData()) : null);
  }
}
