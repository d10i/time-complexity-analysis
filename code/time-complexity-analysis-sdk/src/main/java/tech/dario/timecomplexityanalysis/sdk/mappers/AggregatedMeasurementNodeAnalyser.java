package tech.dario.timecomplexityanalysis.sdk.mappers;

import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.sdk.domain.AggregatedMeasurement;
import tech.dario.timecomplexityanalysis.sdk.domain.InterpolatedMeasurement;
import tech.dario.timecomplexityanalysis.sdk.domain.Probe;
import tech.dario.timecomplexityanalysis.sdk.fitting.*;
import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleNode;

import java.util.*;
import java.util.function.Function;

public class AggregatedMeasurementNodeAnalyser<T extends AbstractNode<AggregatedMeasurement, T>> implements Function<T, SimpleNode<InterpolatedMeasurement>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AggregatedMeasurementNodeAnalyser.class);

  private final static List<FittingFunctionFinder> ALL_FITTING_FUNCTION_FINDERS = new ArrayList<FittingFunctionFinder>() {{
    add(new ConstantFunctionFinder());
    add(new LogarithmicFunctionFinder());
    add(new LinearFunctionFinder());
    add(new LinearithmicFunctionFinder());
    add(new QuadraticFunctionFinder());
    add(new CubicFunctionFinder());
    add(new ExponentialFunctionFinder());
  }};

  private final Probe probe;

  public AggregatedMeasurementNodeAnalyser(Probe probe) {
    this.probe = probe;
  }

  @Override
  public SimpleNode<InterpolatedMeasurement> apply(T node) {
    if (node.getData() == null) {
      return new SimpleNode<>(node.getName(), null);
    }

    LOGGER.debug(node.getName());

    LOGGER.debug("Count:");
    final FittingFunction bestCountFittingFunction = getBestFittingFunction(node, true);
    LOGGER.debug("");
    LOGGER.debug("Average:");
    final FittingFunction bestAverageFittingFunction = getBestFittingFunction(node, false);
    LOGGER.debug("");
    LOGGER.debug("");
    LOGGER.debug("");

    return new SimpleNode<>(node.getName(), new InterpolatedMeasurement(bestCountFittingFunction, bestAverageFittingFunction));
  }

  public FittingFunction getBestFittingFunction(T node, boolean isCount) {
    final Collection<WeightedObservedPoint> observedPoints;
    if (isCount) {
      observedPoints = getObservedPoints(node, this::countObservedPoint);
    } else {
      observedPoints = getObservedPoints(node, this::averageObservedPoint);
    }

    final double averageValue = getAverageValue(observedPoints);
    final double maxValue = getMaxValue(observedPoints);
    LOGGER.debug("Average value: " + averageValue);
    LOGGER.debug("Max value: " + maxValue);
    final List<FittingFunction> fittingFunctions = getFittingFunctions(observedPoints);
    final FittingFunction constantFittingFunction = fittingFunctions.get(0);
    double bestRms = getBestRms(fittingFunctions);
    for (FittingFunction fittingFunction : fittingFunctions) {
      if (isBestFittingFunction(fittingFunction, bestRms, averageValue)) {
        if (!isCount && isAssumedConstant(fittingFunction, constantFittingFunction, maxValue)) {
          return constantFittingFunction;
        }

        return fittingFunction;
      }
    }


    throw new IllegalStateException("None of the fitting functions passed the tolerance test. It could be that the probe tolerance map contains negative values?");
  }

  private List<FittingFunction> getFittingFunctions(final Collection<WeightedObservedPoint> observedPoints) {
    List<FittingFunction> fittingFunctions = new ArrayList<>();
    for (FittingFunctionFinder fittingFunctionFinder : ALL_FITTING_FUNCTION_FINDERS) {
      final Optional<FittingFunction> fittingFunctionOptional = fittingFunctionFinder.findFittingFunction(observedPoints);
      if (fittingFunctionOptional.isPresent()) {
        final FittingFunction fittingFunction = fittingFunctionOptional.get();
        fittingFunctions.add(fittingFunction);
      }
    }
    return fittingFunctions;
  }

  private double getBestRms(List<FittingFunction> fittingFunctions) {
    double bestRms = Double.POSITIVE_INFINITY;
    for (FittingFunction fittingFunction : fittingFunctions) {
      bestRms = Math.min(bestRms, fittingFunction.getRms());
    }
    return bestRms;
  }

  private boolean isBestFittingFunction(final FittingFunction fittingFunction, final double bestRms,
      final double averageValue) {
    LOGGER.debug(fittingFunction.getFittingFunctionType() + ": " + fittingFunction.getRms() + " == " + bestRms + "?");
    LOGGER.debug(fittingFunction.getRms() + " / " + averageValue + " = " + (fittingFunction.getRms() / averageValue));
    if (fittingFunction.getRms() == bestRms) {
      return true;
    }

    return fittingFunction.getRms() / averageValue < probe.getFittingFunctionTolerance();
  }

  private boolean isAssumedConstant(FittingFunction bestFittingFunction, FittingFunction constantFittingFunction,
      double maxValue) {
    if (maxValue < probe.getAssumeConstantThreshold()) {
      double rmsRelativeError = 1 - (bestFittingFunction.getRms() / constantFittingFunction.getRms());
      if (rmsRelativeError < probe.getAssumeConstantMaxRmsRelativeError()) {
        // It's a quick method, assume it doesn't do any work itself, but just calls other instrumented methods. In that
        // case the method time complexity is assumed constant
        LOGGER.warn(
            "Max value {} is lower than threshold {} and the RMS relative error {} is lower than the max {}. "
                + "Returning the least complex fitting function ({}: {})",
            maxValue, probe.getAssumeConstantThreshold(), rmsRelativeError, probe.getAssumeConstantMaxRmsRelativeError(),
            constantFittingFunction.getFittingFunctionType(), constantFittingFunction);
        return true;
      }
    }

    return false;
  }

  private Collection<WeightedObservedPoint> getObservedPoints(T node, Function<Map.Entry<Long, Measurement>, WeightedObservedPoint> conversionFunction) {
    final List<WeightedObservedPoint> observedPoints = new ArrayList<>();
    for (Map.Entry<Long, Measurement> aggregatedData : node.getData().getAggregatedData().entrySet()) {
      observedPoints.add(conversionFunction.apply(aggregatedData));
    }

    return observedPoints;
  }

  private double getAverageValue(Collection<WeightedObservedPoint> observedPoints) {
    double sumY = 0.0d;
    double sumWeight = 0.0d;
    for (WeightedObservedPoint observedPoint : observedPoints) {
      sumY += observedPoint.getY();
      sumWeight += observedPoint.getWeight();
    }

    return sumY / sumWeight;
  }

  private double getMaxValue(Collection<WeightedObservedPoint> observedPoints) {
    double max = Double.NEGATIVE_INFINITY;
    for (WeightedObservedPoint observedPoint : observedPoints) {
      max = Math.max(max, observedPoint.getY());
    }

    return max;
  }

  private WeightedObservedPoint countObservedPoint(Map.Entry<Long, Measurement> aggregatedData) {
    return new WeightedObservedPoint(1.0d, aggregatedData.getKey(), aggregatedData.getValue().getCount());
  }

  private WeightedObservedPoint averageObservedPoint(Map.Entry<Long, Measurement> aggregatedData) {
    return new WeightedObservedPoint(1.0d, aggregatedData.getKey(), aggregatedData.getValue().getTotal() / aggregatedData.getValue().getCount());
  }
}
