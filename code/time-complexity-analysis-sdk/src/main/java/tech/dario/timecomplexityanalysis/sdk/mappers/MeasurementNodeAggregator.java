package tech.dario.timecomplexityanalysis.sdk.mappers;

import tech.dario.timecomplexityanalysis.sdk.domain.AggregatedMeasurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;

import java.util.function.Function;

public class MeasurementNodeAggregator<T extends AbstractNode<Measurement, T>> implements Function<T, MergeableNode<AggregatedMeasurement>> {

  private final long n;

  public MeasurementNodeAggregator(long n) {
    this.n = n;
  }

  @Override
  public MergeableNode<AggregatedMeasurement> apply(T node) {
    if (node.getData() == null) {
      return new MergeableNode<>(node.getName(), null);
    }

    return new MergeableNode<>(node.getName(), new AggregatedMeasurement(n, node.getData()));
  }
}
