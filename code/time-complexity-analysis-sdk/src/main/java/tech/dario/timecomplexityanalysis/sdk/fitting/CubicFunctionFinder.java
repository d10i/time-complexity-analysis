package tech.dario.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

public final class CubicFunctionFinder extends FittingFunctionFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(CubicFunctionFinder.class);

  public CubicFunctionFinder() {
    super(new Parametric(), new double[]{1.0d, 0.0d, 0.0d, 0.0d});
  }

  @Override
  public Optional<FittingFunction> findFittingFunction(Collection<WeightedObservedPoint> points) {
    try {
      LeastSquaresOptimizer.Optimum optimum = getOptimum(points);
      double[] params = optimum.getPoint().toArray();
      return Optional.of(new CubicFunction(params[0], params[1], params[2], params[3], optimum.getRMS()));
    } catch (Exception e) {
      LOGGER.warn("Unable to find fitting cubic function", e);
      return Optional.empty();
    }
  }

  @Override
  public String getName() {
    return "Cubic";
  }

  @Override
  public RealVector validate(RealVector realVector) {
    // Constraints:
    // 1. f(1) > 0:                   a + b + c + d > 0
    // 2. increasing-only for n >= 0: a > 0 and c >= (b ^ 2) / (3 * a)
    // 3. flexing point at n <= 0:    b >= 0
    double a = Math.max(realVector.getEntry(0), Double.MIN_VALUE); // 2a
    double b = Math.max(realVector.getEntry(1), 0.0d); // 3
    double c = Math.max(realVector.getEntry(2), Math.pow(b, 2) / (3 * a)); // 2b
    double d = Math.max(realVector.getEntry(3), Double.MIN_VALUE - a - b - c); // 1
    realVector.setEntry(0, a);
    realVector.setEntry(1, b);
    realVector.setEntry(2, c);
    realVector.setEntry(3, d);
    return realVector;
  }

  private class CubicFunction implements FittingFunction {
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double rms;

    public CubicFunction(double a, double b, double c, double d, double rms) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
      this.rms = rms;
    }

    @Override
    public double f(double n) {
      return a * n * n * n + b * n * n + c * n + d;
    }

    @Override
    public double getRms() {
      return rms;
    }

    @Override
    public String toString() {
      return String.format("%.6f * n^3 + %.6f * n^2 + %.6f * n + %.6f", a, b, c, d);
    }
  }

  private static class Parametric implements ParametricUnivariateFunction {
    @Override
    public double[] gradient(double x, double[] params) {
      return new double[]{x * x * x, x * x, x, 1.0d};
    }

    @Override
    public double value(double x, double[] params) {
      double a = params[0];
      double b = params[1];
      double c = params[2];
      double d = params[3];
      return a * x * x * x + b * x * x + c * x + d;
    }
  }
}
