package tech.dario.dissertation.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealVector;

import java.util.Collection;

public final class ConstantFunctionFinder extends FittingFunctionFinder {
  public ConstantFunctionFinder() {
    super(new Parametric(), new double[]{0.0d});
  }

  @Override
  public FittingFunction findFittingFunction(Collection<WeightedObservedPoint> points) {
    LeastSquaresOptimizer.Optimum optimum = getOptimum(points);
    double[] params = optimum.getPoint().toArray();
    return new ConstantFunction(params[0], optimum.getRMS());
  }

  @Override
  public RealVector validate(RealVector realVector) {
    // Constraints:
    // a >= 0
    double a = Math.max(realVector.getEntry(0), 0.0d);
    realVector.setEntry(0, a);
    return realVector;
  }

  private class ConstantFunction implements FittingFunction {
    private final double a;
    private final double rms;

    private ConstantFunction(double a, double rms) {
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
    public String toString() {
      return String.format("%.6f [rms: %.6f]", a, rms);
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
