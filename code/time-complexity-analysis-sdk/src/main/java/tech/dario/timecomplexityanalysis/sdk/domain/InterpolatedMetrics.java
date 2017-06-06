package tech.dario.timecomplexityanalysis.sdk.domain;

import tech.dario.timecomplexityanalysis.sdk.fitting.FittingFunction;

public class InterpolatedMetrics {

  private final FittingFunction countFunction;
  private final FittingFunction averageFunction;

  public InterpolatedMetrics(final FittingFunction countFunction, final FittingFunction averageFunction) {
    this.countFunction = countFunction;
    this.averageFunction = averageFunction;
  }

  public FittingFunction getCountFunction() {
    return countFunction;
  }

  public FittingFunction getAverageFunction() {
    return averageFunction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    InterpolatedMetrics that = (InterpolatedMetrics) o;

    if (countFunction != null ? !countFunction.equals(that.countFunction) : that.countFunction != null) return false;
    return averageFunction != null ? averageFunction.equals(that.averageFunction) : that.averageFunction == null;
  }

  @Override
  public int hashCode() {
    int result = countFunction != null ? countFunction.hashCode() : 0;
    result = 31 * result + (averageFunction != null ? averageFunction.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "{countFunction: " + countFunction +
        ", averageFunction: " + averageFunction + "}";
  }
}
