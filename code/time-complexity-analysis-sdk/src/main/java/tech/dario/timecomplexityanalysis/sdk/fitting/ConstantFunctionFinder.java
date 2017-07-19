package tech.dario.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

public final class ConstantFunctionFinder extends FittingFunctionFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConstantFunctionFinder.class);

  public ConstantFunctionFinder() {
    super(new Parametric(), new double[]{0.0d});
  }

  @Override
  public Optional<FittingFunction> findFittingFunction(Collection<WeightedObservedPoint> points) {
    try {
      LeastSquaresOptimizer.Optimum optimum = getOptimum(points);
      double[] params = optimum.getPoint().toArray();
      if(allParamsValid(params)) {
        return Optional.of(new ConstantFunction(params[0], optimum.getRMS()));
      }
    } catch (Exception e) {
      LOGGER.warn("Unable to find fitting constant function", e);
    }

    return Optional.empty();
  }

  @Override
  public String getName() {
    return "Constant";
  }

  @Override
  public RealVector validate(RealVector realVector) {
    // Constraints:
    // 1. f(1) > 0: a > 0
    double a = Math.max(realVector.getEntry(0), Double.MIN_VALUE); // 1
    realVector.setEntry(0, a);
    return realVector;
  }

  public static class ConstantFunction implements FittingFunction {
    private final double a;
    private final double rms;

    public ConstantFunction(double a, double rms) {
      this.a = a;
      this.rms = rms;
    }

    @Override
    public double f(double n) {
      return a;
    }

    @Override
    public double getRms() {
      return rms;
    }

    @Override
    public FittingFunctionType getFittingFunctionType() {
      return FittingFunctionType.CONSTANT;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof ConstantFunction)) {
        return false;
      }

      ConstantFunction that = (ConstantFunction) o;

      if (Double.compare(that.a, a) != 0) {
        return false;
      }
      return Double.compare(that.rms, rms) == 0;
    }

    @Override
    public int hashCode() {
      int result;
      long temp;
      temp = Double.doubleToLongBits(a);
      result = (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(rms);
      result = 31 * result + (int) (temp ^ (temp >>> 32));
      return result;
    }

    @Override
    public String toString() {
      return String.format("%.15e", a);
    }
  }

  private static class Parametric implements ParametricUnivariateFunction {
    @Override
    public double[] gradient(double x, double[] params) {
      return new double[]{1.0d};
    }

    @Override
    public double value(double x, double[] params) {
      return params[0];
    }
  }
}
