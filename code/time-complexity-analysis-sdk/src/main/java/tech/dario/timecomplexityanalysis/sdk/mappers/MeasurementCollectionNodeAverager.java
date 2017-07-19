package tech.dario.timecomplexityanalysis.sdk.mappers;

import java.util.Collection;
import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableCollection;

import java.util.function.Function;

public class MeasurementCollectionNodeAverager<T extends AbstractNode<MergeableCollection<Measurement>, T>> implements Function<T, MergeableNode<Measurement>> {
  @Override
  public MergeableNode<Measurement> apply(T node) {
    if (node.getData() == null) {
      return new MergeableNode<>(node.getName(), null);
    }

    final Collection<Measurement> measurementCollection = node.getData().getCollection();
    final Measurement averageMeasurement = computeAverageMeasurement(measurementCollection);
    return new MergeableNode<>(node.getName(), averageMeasurement);
  }

  private Measurement computeAverageMeasurement(Collection<Measurement> measurementCollection) {
    final int numMeasurement = measurementCollection.size();
    double countSum = 0.0d;
    double totalSum = 0.0d;
    for (Measurement measurement : measurementCollection) {
      countSum += measurement.getCount();
      totalSum += measurement.getTotal();
    }

    return new Measurement(countSum / numMeasurement, totalSum / numMeasurement);
  }
}
