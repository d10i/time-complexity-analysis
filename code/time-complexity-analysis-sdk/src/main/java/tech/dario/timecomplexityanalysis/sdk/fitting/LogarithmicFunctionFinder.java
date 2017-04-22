package tech.dario.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

public final class LogarithmicFunctionFinder extends FittingFunctionFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogarithmicFunctionFinder.class);

  public LogarithmicFunctionFinder() {
    super(new Parametric(), new double[]{1.0d, 0.0d});
  }

  @Override
  public Optional<FittingFunction> findFittingFunction(Collection<WeightedObservedPoint> points) {
    try {
      LeastSquaresOptimizer.Optimum optimum = getOptimum(points);
      double[] params = optimum.getPoint().toArray();
      return Optional.of(new LogarithmicFunction(params[0], params[1], optimum.getRMS()));
    } catch (Exception e) {
      LOGGER.warn("Unable to find fitting logarithmic function", e);
      return Optional.empty();
    }
  }

  @Override
  public String getName() {
    return "Logarithmic";
  }

  @Override
  public RealVector validate(RealVector realVector) {
    // Constraints:
    // 1. f(1) > 0:                   b > 0
    // 2. increasing-only for n >= 0: a > 0
    double a = Math.max(realVector.getEntry(0), Double.MIN_VALUE); // 2
    double b = Math.max(realVector.getEntry(1), Double.MIN_VALUE); // 1
    realVector.setEntry(0, a);
    realVector.setEntry(1, b);
    return realVector;
  }

  private class LogarithmicFunction implements FittingFunction {
    private final double a;
    private final double b;
    private final double rms;

    private LogarithmicFunction(double a, double b, double rms) {
      this.a = a;
      this.b = b;
      this.rms = rms;
    }

    @Override
    public double f(double n) {
      return a * Math.log(n) + b;
    }

    @Override
    public double getRms() {
      return rms;
    }

    @Override
    public String toString() {
      return String.format("%.6f * ln(n) + %.6f", a, b);
    }
  }

  private static class Parametric implements ParametricUnivariateFunction {
    @Override
    public double[] gradient(double x, double[] params) {
      return new double[]{Math.log(x), 1.0d};
    }

    @Override
    public double value(double x, double[] params) {
      double a = params[0];
      double b = params[1];
      return a * Math.log(x) + b;
    }
  }
}
