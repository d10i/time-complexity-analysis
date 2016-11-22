package tech.dario.dissertation.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;

import java.util.Collection;

public final class LogarithmicFunctionFinder extends FittingFunctionFinder {

  public LogarithmicFunctionFinder() {
    super(new Parametric(), new double[]{1.0d, 0.0d});
  }

  @Override
  public FittingFunction findFittingFunction(Collection<WeightedObservedPoint> points) {
    LeastSquaresOptimizer.Optimum optimum = getOptimum(points);
    double[] params = optimum.getPoint().toArray();
    return new Function(params[0], params[1], optimum.getRMS());
  }

  private class Function implements FittingFunction {
    private final double a;
    private final double b;
    private final double rms;

    private Function(double a, double b, double rms) {
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
    public String getStringRepresentation() {
      return String.format("%.3f * ln(n) + %.3f [rms: %.3f]", a, b, rms);
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
