package tech.dario.timecomplexityanalysis.sdk.mappers;

import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import tech.dario.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.timecomplexityanalysis.sdk.fitting.*;
import tech.dario.timecomplexityanalysis.timerecorder.tree.AbstractNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Metrics;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleNode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.function.Function;

import static java.awt.Color.*;

public class AggregatedMetricsNodeAnalyser<T extends AbstractNode<AggregatedMetrics, T>> implements Function<T, SimpleNode<FittingFunction>> {
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
  public SimpleNode<FittingFunction> apply(T node) {
    if (node.getData() == null) {
      return new SimpleNode<>(node.getName(), null);
    }

    Collection<WeightedObservedPoint> observedPoints = getObservedPoints(node);

    System.out.println();
    System.out.println("---");
    System.out.println(node.getName());
    System.out.println("---");

    long maxN = 1005L;
    double maxT = Double.NEGATIVE_INFINITY;

    double bestRms = Double.POSITIVE_INFINITY;
    FittingFunction bestFittingFunction = null;
    XYSeriesCollection dataset = new XYSeriesCollection();
    for (FittingFunctionFinder fittingFunctionFinder : FITTING_FUNCTION_FINDER_LIST) {
      Optional<FittingFunction> currentFittingFunctionOptional = fittingFunctionFinder.findFittingFunction(observedPoints);

      if(currentFittingFunctionOptional.isPresent()) {
        FittingFunction currentFittingFunction = currentFittingFunctionOptional.get();
        XYSeries currentSeries = new XYSeries(currentFittingFunction.getClass().getSimpleName());
        for (double n = 0L; n < maxN; n += maxN / 200.0d) {
          double t = currentFittingFunction.f(n);
          if (t > maxT) {
            maxT = t;
          }

          currentSeries.add(n, t);
        }
        dataset.addSeries(currentSeries);

        if (currentFittingFunction.getRms() < bestRms) {
          bestRms = currentFittingFunction.getRms();
          bestFittingFunction = currentFittingFunction;
        }
        System.out.println(fittingFunctionFinder.getName() + ";" + currentFittingFunction.getRms() + ";" + currentFittingFunction.toString());
      } else {
        System.out.println("N/A");
      }
    }

    XYSeries actualSeries = new XYSeries("Actual");
    for (Map.Entry<Long, Metrics> aggregatedData : node.getData().getAggregatedData().entrySet()) {
      double t = aggregatedData.getValue().getTotal() / aggregatedData.getValue().getCount();
      if(t > maxT) {
        maxT = t;
      }

      actualSeries.add((double) aggregatedData.getKey(), t);
    }
    dataset.addSeries(actualSeries);

    try {
      saveGraph(node.getName(), maxN, maxT, dataset);
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println("---");

    return new SimpleNode<>(node.getName(), bestFittingFunction);
  }

  private Collection<WeightedObservedPoint> getObservedPoints(T node) {
    final List<WeightedObservedPoint> observedPoints = new ArrayList<>();
    for (Map.Entry<Long, Metrics> aggregatedData : node.getData().getAggregatedData().entrySet()) {
      observedPoints.add(aggregatedDataToWeightedObservedPoint(aggregatedData));
    }

    return observedPoints;
  }

  private WeightedObservedPoint aggregatedDataToWeightedObservedPoint(Map.Entry<Long, Metrics> aggregatedData) {
    return new WeightedObservedPoint(1.0d, aggregatedData.getKey(), aggregatedData.getValue().getTotal() / aggregatedData.getValue().getCount());
  }

  private void saveGraph(String name, long maxN, double maxT, XYSeriesCollection dataset) throws Exception {
    JFreeChart chart = ChartFactory.createXYLineChart(
            null,                     // chart title
            "n",                      // x axis label
            "t",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // plot orientation
            true,                     // include legend
            false,                    // tooltips
            false                     // urls
    );

    chart.setBackgroundPaint(BLACK);

    XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(LIGHT_GRAY);
    plot.setDomainGridlinePaint(WHITE);
    plot.setRangeGridlinePaint(WHITE);
    plot.getDomainAxis().setTickLabelPaint(WHITE);
    plot.getRangeAxis().setTickLabelPaint(WHITE);

    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setBaseStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

    // Fitting functions
    renderer.setBaseLinesVisible(true);
    renderer.setBaseShapesVisible(false);
    renderer.setAutoPopulateSeriesStroke(true);
    renderer.setSeriesPaint(0, GREEN);
    renderer.setSeriesPaint(1, BLUE);
    renderer.setSeriesPaint(2, RED);
    renderer.setSeriesPaint(3, YELLOW);
    renderer.setSeriesPaint(4, PINK);
    renderer.setSeriesPaint(5, CYAN);
    renderer.setSeriesPaint(6, ORANGE);

    // Actual data
    renderer.setSeriesPaint(7, BLACK);
    renderer.setSeriesLinesVisible(7, false);
    renderer.setSeriesShapesVisible(7, true);

    plot.setRenderer(renderer);

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setRange(0.0d, maxT);
    rangeAxis.setTickUnit(new NumberTickUnit(Math.round(maxT / 8.0d)));

    NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
    domainAxis.setRange(0.0d, maxN);
    domainAxis.setTickUnit(new NumberTickUnit(Math.round(maxN / 8.0d)));

    BufferedImage bufferedImage = chart.createBufferedImage(1200, 675);
    File outputFile = new File(name + "-" + System.nanoTime() + "-chart.png");
    ImageIO.write(bufferedImage, "png", outputFile);
  }
}
