package tech.dario.dissertation.timerecorder.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetricsList implements MergeableValue<MetricsList> {
  private List<Metrics> list;

  public MetricsList(List<Metrics> list) {
    this.list = list;
  }

  public static MetricsList fromMetrics(Metrics metrics) {
    List<Metrics> list = new ArrayList<>();
    list.add(metrics);
    return new MetricsList(list);
  }

  public static MetricsList empty() {
    return new MetricsList(new ArrayList<>());
  }

  public List<Metrics> getList() {
    return list;
  }

  @Override
  public MetricsList mergeWith(MetricsList otherMetricsList) {
    this.list.addAll(otherMetricsList.getList());
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MetricsList that = (MetricsList) o;

    return list != null ? list.equals(that.list) : that.list == null;

  }

  @Override
  public int hashCode() {
    return list != null ? list.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "MetricsList[" +
            Arrays.toString(list.toArray()) +
            ']';
  }
}
