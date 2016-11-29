package tech.dario.dissertation.timecomplexityanalysis.sdk.domain;

import tech.dario.dissertation.timerecorder.tree.MergeableValue;
import tech.dario.dissertation.timerecorder.tree.Metrics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AggregatedMetrics implements MergeableValue<AggregatedMetrics> {

  private final Map<Long, Metrics> aggregatedData;

  public AggregatedMetrics(long n, Metrics metrics) {
    this.aggregatedData = new HashMap<>();
    this.aggregatedData.put(n, metrics);
  }

  @Override
  public AggregatedMetrics mergeWith(AggregatedMetrics aggregatedMetrics) {
    this.aggregatedData.putAll(aggregatedMetrics.getAggregatedData());
    return this;
  }

  private Map<Long, Metrics> getAggregatedData() {
    return aggregatedData;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    Iterator<Map.Entry<Long, Metrics>> iter = aggregatedData.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<Long, Metrics> entry = iter.next();
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
