package tech.dario.dissertation.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealVector;

import java.util.Collection;

public final class ExponentialFunctionFinder extends FittingFunctionFinder {

  public ExponentialFunctionFinder() {
    super(new Parametric(), new double[]{2.0d, 1.0d, 0.0d, 0.0d});
  }

  @Override
  public FittingFunction findFittingFunction(Collection<WeightedObservedPoint> points) {
    LeastSquaresOptimizer.Optimum optimum = getOptimum(points);
    double[] params = optimum.getPoint().toArray();
    return new ExponentialFunction(params[0], params[1], params[2], params[3], optimum.getRMS());
  }

  @Override
  public RealVector validate(RealVector realVector) {
    // Constraints:
    // a > 1
    // b > 0
    // a^(b+c)+d >= 0
    double a = Math.max(realVector.getEntry(0), 1.0d + Double.MIN_VALUE);
    double b = Math.max(realVector.getEntry(1), 0.0d + Double.MIN_VALUE);
    double c = realVector.getEntry(2);
    double d = Math.max(realVector.getEntry(3), -Math.pow(a, b + c));
    realVector.setEntry(0, a);
    realVector.setEntry(1, b);
    realVector.setEntry(2, c);
    realVector.setEntry(3, d);
    return realVector;
  }

  private class ExponentialFunction implements FittingFunction {
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double rms;

    public ExponentialFunction(double a, double b, double c, double d, double rms) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
      this.rms = rms;
    }

    @Override
    public double f(double n) {
      return Math.pow(a, n * b + c) + d;
    }

    @Override
    public double getRms() {
      return rms;
    }

    @Override
    public String toString() {
      return String.format("%.6f ^ (n * %.6f + %.6f) + %.6f [rms: %.6f]", a, b, c, d, rms);
    }
  }

  private static class Parametric implements ParametricUnivariateFunction {
    @Override
    public double[] gradient(double x, double[] params) {
      double a = params[0];
      double b = params[1];
      double c = params[2];
      return new double[]{
              Math.pow(a, b * x + c - 1) * (b * x + c),
              Math.pow(a, b * x + c) * x * Math.log(a),
              Math.pow(a, b * x + c) * Math.log(a),
              1.0d
      };
    }

    @Override
    public double value(double x, double[] params) {
      double a = params[0];
      double b = params[1];
      double c = params[2];
      double d = params[3];
      return Math.pow(a, x * b + c) + d;
    }
  }
}
