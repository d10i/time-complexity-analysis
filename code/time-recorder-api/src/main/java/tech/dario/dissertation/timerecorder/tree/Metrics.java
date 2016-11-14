package tech.dario.dissertation.timerecorder.tree;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Metrics implements MergeableValue<Metrics> {
  private long count;
  private double total;
  private double min;
  private double max;

  private static final DecimalFormat DECIMAL_FORMAT;
  static {
    DECIMAL_FORMAT = new DecimalFormat();
    DECIMAL_FORMAT.setMaximumFractionDigits(1);
    DECIMAL_FORMAT.setMinimumFractionDigits(1);
    DECIMAL_FORMAT.setGroupingSize(3);
    DECIMAL_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    DECIMAL_FORMAT.setDecimalSeparatorAlwaysShown(true);
    DECIMAL_FORMAT.setGroupingUsed(true);
  }

  private Metrics() {
  }

  public Metrics(long count, double total, double min, double max) {
    this.count = count;
    this.total = total;
    this.min = min;
    this.max = max;
  }

  public static Metrics fromElapsedTime(long elapsedTime) {
    return new Metrics(1, elapsedTime, elapsedTime, elapsedTime);
  }

  @Override
  public Metrics mergeWith(Metrics otherMetrics) {
    this.count += otherMetrics.count;
    this.total += otherMetrics.total;
    this.min = Math.min(this.min, otherMetrics.min);
    this.max = Math.max(this.max, otherMetrics.max);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Metrics metrics = (Metrics) o;

    if (count != metrics.count) return false;
    if (Double.compare(metrics.total, total) != 0) return false;
    if (Double.compare(metrics.min, min) != 0) return false;
    return Double.compare(metrics.max, max) == 0;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = (int) (count ^ (count >>> 32));
    temp = Double.doubleToLongBits(total);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(min);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(max);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "[count: " + count +
            ", avg: " + toMicroSeconds(total / count) +
            ", min: " + toMicroSeconds(min) +
            ", max: " + toMicroSeconds(max) + "]";
  }

  private String toMicroSeconds(double nanoSeconds) {
    return DECIMAL_FORMAT.format(nanoSeconds / 1000) + " μs";
  }
}
