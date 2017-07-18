package tech.dario.timecomplexityanalysis.sdk.domain;

import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableValue;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AggregatedMeasurement implements MergeableValue<AggregatedMeasurement> {

  private final Map<Long, Measurement> aggregatedData;

  public AggregatedMeasurement(long n, Measurement measurement) {
    this.aggregatedData = new HashMap<>();
    this.aggregatedData.put(n, measurement);
  }

  @Override
  public AggregatedMeasurement mergeWith(AggregatedMeasurement aggregatedMeasurement) {
    this.aggregatedData.putAll(aggregatedMeasurement.getAggregatedData());
    return this;
  }

  public Map<Long, Measurement> getAggregatedData() {
    return aggregatedData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AggregatedMeasurement that = (AggregatedMeasurement) o;

    return aggregatedData != null ? aggregatedData.equals(that.aggregatedData) : that.aggregatedData == null;

  }

  @Override
  public int hashCode() {
    return aggregatedData != null ? aggregatedData.hashCode() : 0;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    Iterator<Map.Entry<Long, Measurement>> iter = aggregatedData.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<Long, Measurement> entry = iter.next();
      sb.append(entry.getKey());
      sb.append(": ");
      sb.append(entry.getValue());
      if (iter.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("}");
    return sb.toString();
  }
}
