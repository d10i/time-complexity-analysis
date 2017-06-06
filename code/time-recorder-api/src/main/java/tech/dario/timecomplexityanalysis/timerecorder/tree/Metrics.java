package tech.dario.timecomplexityanalysis.timerecorder.tree;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Metrics implements MergeableValue<Metrics> {
  private double count;
  private double total;

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

  public Metrics(double count, double total) {
    this.count = count;
    this.total = total;
  }

  public static Metrics fromElapsedTime(long elapsedTime) {
    return new Metrics(1, elapsedTime);
  }

  public double getCount() {
    return count;
  }

  public double getTotal() {
    return total;
  }

  @Override
  public Metrics mergeWith(Metrics otherMetrics) {
    this.count += otherMetrics.count;
    this.total += otherMetrics.total;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Metrics metrics = (Metrics) o;

    if (Double.compare(metrics.count, count) != 0) return false;
    return Double.compare(metrics.total, total) == 0;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(count);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(total);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "{count: " + count +
            ", tot: " + toMicroSeconds(total) +
            ", avg: " + toMicroSeconds(total / count) + "}";
  }

  private String toMicroSeconds(double nanoSeconds) {
    return DECIMAL_FORMAT.format(nanoSeconds / 1000) + " μs";
  }
}
