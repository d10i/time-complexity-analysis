package tech.dario.timecomplexityanalysis.sdk.mappers;

import org.apache.commons.math3.fitting.WeightedObservedPoint;
import tech.dario.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.timecomplexityanalysis.sdk.domain.InterpolatedMetrics;
import tech.dario.timecomplexityanalysis.sdk.fitting.*;
import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Metrics;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleNode;

import java.util.*;
import java.util.List;
import java.util.function.Function;

public class AggregatedMetricsNodeAnalyser<T extends AbstractNode<AggregatedMetrics, T>> implements Function<T, SimpleNode<InterpolatedMetrics>> {
  private final static List<FittingFunctionFinder> FITTING_FUNCTION_FINDER_LIST = new ArrayList<FittingFunctionFinder>() {{
    add(new ConstantFunctionFinder());
    add(new LinearFunctionFinder());
    add(new LinearithmicFunctionFinder());
    add(new LogarithmicFunctionFinder());
    add(new ExponentialFunctionFinder());
    add(new QuadraticFunctionFinder());
    add(new CubicFunctionFinder());
  }};

  @Override
  public SimpleNode<InterpolatedMetrics> apply(T node) {
    if (node.getData() == null) {
      return new SimpleNode<>(node.getName(), null);
    }

    final FittingFunction bestCountFittingFunction = getBestFittingFunction(node, this::countObservedPoint);
    final FittingFunction bestAverageFittingFunction = getBestFittingFunction(node, this::averageObservedPoint);

    return new SimpleNode<>(node.getName(), new InterpolatedMetrics(bestCountFittingFunction, bestAverageFittingFunction));
  }

  private FittingFunction getBestFittingFunction(T node, Function<Map.Entry<Long, Metrics>, WeightedObservedPoint> conversionFunction) {
    Collection<WeightedObservedPoint> observedPoints = getObservedPoints(node, conversionFunction);
    double bestRms = Double.POSITIVE_INFINITY;
    FittingFunction bestFittingFunction = null;
    for (FittingFunctionFinder fittingFunctionFinder : FITTING_FUNCTION_FINDER_LIST) {
      Optional<FittingFunction> currentFittingFunctionOptional = fittingFunctionFinder.findFittingFunction(observedPoints);
      if(currentFittingFunctionOptional.isPresent()) {
        FittingFunction currentFittingFunction = currentFittingFunctionOptional.get();
        if (currentFittingFunction.getRms() < bestRms) {
          bestRms = currentFittingFunction.getRms();
          bestFittingFunction = currentFittingFunction;
        }
      }
    }
    return bestFittingFunction;
  }

  private Collection<WeightedObservedPoint> getObservedPoints(T node, Function<Map.Entry<Long, Metrics>, WeightedObservedPoint> conversionFunction) {
    final List<WeightedObservedPoint> observedPoints = new ArrayList<>();
    for (Map.Entry<Long, Metrics> aggregatedData : node.getData().getAggregatedData().entrySet()) {
      observedPoints.add(conversionFunction.apply(aggregatedData));
    }

    return observedPoints;
  }

  private WeightedObservedPoint countObservedPoint(Map.Entry<Long, Metrics> aggregatedData) {
    return new WeightedObservedPoint(1.0d, aggregatedData.getKey(), aggregatedData.getValue().getCount());
  }

  private WeightedObservedPoint averageObservedPoint(Map.Entry<Long, Metrics> aggregatedData) {
    return new WeightedObservedPoint(1.0d, aggregatedData.getKey(), aggregatedData.getValue().getTotal() / aggregatedData.getValue().getCount());
  }
}
