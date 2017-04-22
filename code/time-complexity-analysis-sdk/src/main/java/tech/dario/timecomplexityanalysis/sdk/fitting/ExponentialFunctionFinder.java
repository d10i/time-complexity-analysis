package tech.dario.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

public final class ExponentialFunctionFinder extends FittingFunctionFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExponentialFunctionFinder.class);

  public ExponentialFunctionFinder() {
    super(new Parametric(), new double[]{0.001d, 0.01d, 0.0d});
  }

  @Override
  public Optional<FittingFunction> findFittingFunction(Collection<WeightedObservedPoint> points) {
    try {
      LeastSquaresOptimizer.Optimum optimum = getOptimum(points);
      double[] params = optimum.getPoint().toArray();
      return Optional.of(new ExponentialFunction(params[0], params[1], params[2], optimum.getRMS()));
    } catch (Exception e) {
      LOGGER.warn("Unable to find fitting exponential function", e);
      return Optional.empty();
    }
  }

  @Override
  public String getName() {
    return "Exponential";
  }

  @Override
  public RealVector validate(RealVector realVector) {
    // Constraints:
    // 1. f(1) > 0:                   a * e ^ b + c > 0
    // 2. increasing-only for n >= 0: a > 0 and b > 0
    double a = Math.max(realVector.getEntry(0), 0.0d + Double.MIN_VALUE); // 2a
    double b = Math.max(realVector.getEntry(1), 0.0d + Double.MIN_VALUE); // 2b
    double c = Math.max(realVector.getEntry(2), Double.MIN_VALUE - a * Math.pow(Math.E, b)); // 1
    realVector.setEntry(0, a);
    realVector.setEntry(1, b);
    realVector.setEntry(2, c);
    return realVector;
  }

//  @Override
//  public RealVector validate(RealVector realVector) {
//
//    // Math.pow(a, x * b + c) + d;
//    // f(x) = 0.00218014 e ^ (0.0147812 * x)
//    // a = e
//    // b = 0.0147812
//    // c = ln(0.00218014) = −6.128366184
//    // d = 0.0
//
//    // 1.  ok
//    // 2a. ok
//    // 2b. ok
//    return realVector;
//  }

  private class ExponentialFunction implements FittingFunction {
    private final double a;
    private final double b;
    private final double c;
    private final double rms;

    public ExponentialFunction(double a, double b, double c, double rms) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.rms = rms;
    }

    @Override
    public double f(double n) {
      return a * Math.pow(Math.E, b * n) + c;
    }

    @Override
    public double getRms() {
      return rms;
    }

    @Override
    public String toString() {
      return String.format("%.6f * e ^ (n * %.6f) + %.6f", a, b, c);
    }
  }

  private static class Parametric implements ParametricUnivariateFunction {
    @Override
    public double[] gradient(double x, double[] params) {
      double a = params[0];
      double b = params[1];
      return new double[]{
              Math.pow(Math.E, b * x),
              a * x * Math.pow(Math.E, b * x),
              1.0d
      };
    }

    @Override
    public double value(double x, double[] params) {
      double a = params[0];
      double b = params[1];
      double c = params[2];
      return a * Math.pow(Math.E, b * x) + c;
    }
  }
}
