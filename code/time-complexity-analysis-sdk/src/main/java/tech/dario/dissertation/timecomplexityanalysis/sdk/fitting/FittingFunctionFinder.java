package tech.dario.dissertation.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.ParameterValidator;
import org.apache.commons.math3.linear.DiagonalMatrix;

import java.util.Collection;

public abstract class FittingFunctionFinder implements ParameterValidator {
  private final CustomCurveFitter customCurveFitter;

  protected FittingFunctionFinder(ParametricUnivariateFunction function, double[] initialGuess) {
    this.customCurveFitter = new CustomCurveFitter(function, initialGuess, this);
  }

  public abstract FittingFunction findFittingFunction(Collection<WeightedObservedPoint> points);

  protected LeastSquaresOptimizer.Optimum getOptimum(Collection<WeightedObservedPoint> points) {
    return customCurveFitter.getOptimizer().optimize(customCurveFitter.getProblem(points));
  }

  private class CustomCurveFitter extends AbstractCurveFitter {
    private final ParametricUnivariateFunction function;
    private final double[] initialGuess;
    private final ParameterValidator parameterValidator;

    public CustomCurveFitter(ParametricUnivariateFunction function, double[] initialGuess, ParameterValidator parameterValidator) {
      this.function = function;
      this.initialGuess = initialGuess;
      this.parameterValidator = parameterValidator;
    }

    @Override
    protected LeastSquaresOptimizer getOptimizer() {
      return super.getOptimizer();
    }

    @Override
    // Same as SimpleCurveFitter
    // Could not extend from it because the constructor is private
    // Could not use it in a field as composition as getProblem is protected
    protected LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> observations) {
      // Prepare least-squares problem.
      final int len = observations.size();
      final double[] target = new double[len];
      final double[] weights = new double[len];

      int count = 0;
      for (WeightedObservedPoint obs : observations) {
        target[count] = obs.getY();
        weights[count] = obs.getWeight();
        ++count;
      }

      final AbstractCurveFitter.TheoreticalValuesFunction model = new AbstractCurveFitter.TheoreticalValuesFunction(function, observations);

      // Create an optimizer for fitting the curve to the observed points.
      return new LeastSquaresBuilder().
              maxEvaluations(Integer.MAX_VALUE).
              maxIterations(Integer.MAX_VALUE).
              start(initialGuess).
              target(target).
              weight(new DiagonalMatrix(weights)).
              model(model.getModelFunction(), model.getModelFunctionJacobian()).
              parameterValidator(parameterValidator).
              build();
    }
  }
}
