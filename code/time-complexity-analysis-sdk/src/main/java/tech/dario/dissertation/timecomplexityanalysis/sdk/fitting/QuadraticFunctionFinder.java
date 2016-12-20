package tech.dario.dissertation.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

public final class QuadraticFunctionFinder extends FittingFunctionFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuadraticFunctionFinder.class);

  public QuadraticFunctionFinder() {
    super(new Parametric(), new double[]{1.0d, 1.0d, 0.0d});
  }

  @Override
  public Optional<FittingFunction> findFittingFunction(Collection<WeightedObservedPoint> points) {
    try {
      LeastSquaresOptimizer.Optimum optimum = getOptimum(points);
      double[] params = optimum.getPoint().toArray();
      return Optional.of(new QuadraticFunction(params[0], params[1], params[2], optimum.getRMS()));
    } catch (Exception e) {
      LOGGER.warn("Unable to find fitting quadratic function", e);
      return Optional.empty();
    }
  }

  @Override
  public RealVector validate(RealVector realVector) {
    // Constraints:
    // a > 0
    // b >= 0
    // a + b + c >= 0
    double a = Math.max(realVector.getEntry(0), 0.0d + Double.MIN_VALUE);
    double b = Math.max(realVector.getEntry(1), 0.0d);
    double c = Math.max(realVector.getEntry(2), -a - b);
    realVector.setEntry(0, a);
    realVector.setEntry(1, b);
    realVector.setEntry(2, c);
    return realVector;
  }

  private class QuadraticFunction implements FittingFunction {
    private final double a;
    private final double b;
    private final double c;
    private final double rms;

    private QuadraticFunction(double a, double b, double c, double rms) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.rms = rms;
    }

    @Override
    public double f(double n) {
      return a * n * n + b * n + c;
    }

    @Override
    public double getRms() {
      return rms;
    }

    @Override
    public String toString() {
      return String.format("%.6f * n^2 + %.6f * n + %.6f [rms: %.6f]", a, b, c, rms);
    }
  }

  private static class Parametric implements ParametricUnivariateFunction {
    @Override
    public double[] gradient(double x, double[] params) {
      return new double[]{x * x, x, 1.0d};
    }

    @Override
    public double value(double x, double[] params) {
      double a = params[0];
      double b = params[1];
      double c = params[2];
      return a * x * x + b * x + c;
    }
  }
}
