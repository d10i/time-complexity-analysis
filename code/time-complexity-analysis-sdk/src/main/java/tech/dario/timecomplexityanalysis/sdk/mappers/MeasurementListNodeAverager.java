package tech.dario.timecomplexityanalysis.sdk.mappers;

import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableList;

import java.util.List;
import java.util.function.Function;

public class MeasurementListNodeAverager<T extends AbstractNode<MergeableList<Measurement>, T>> implements Function<T, MergeableNode<Measurement>> {
  @Override
  public MergeableNode<Measurement> apply(T node) {
    if (node.getData() == null) {
      return new MergeableNode<>(node.getName(), null);
    }

    final List<Measurement> measurementList = node.getData().getList();
    final Measurement averageMeasurement = computeAverageMeasurement(measurementList);
    return new MergeableNode<>(node.getName(), averageMeasurement);
  }

  private Measurement computeAverageMeasurement(List<Measurement> measurementList) {
    final int numMeasurement = measurementList.size();
    double countSum = 0.0d;
    double totalSum = 0.0d;
    for (Measurement measurement : measurementList) {
      countSum += measurement.getCount();
      totalSum += measurement.getTotal();
    }

    return new Measurement(countSum / numMeasurement, totalSum / numMeasurement);
  }
}
