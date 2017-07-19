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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.sdk.domain.AggregatedMeasurement;
import tech.dario.timecomplexityanalysis.sdk.fitting.*;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.function.Function;

import static java.awt.Color.*;

public class AggregatedMeasurementNodeDebugger implements Function<MergeableNode<AggregatedMeasurement>, MergeableNode<AggregatedMeasurement>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AggregatedMeasurementNodeDebugger.class);

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
  public MergeableNode<AggregatedMeasurement> apply(MergeableNode<AggregatedMeasurement> node) {
    if (node.getData() == null) {
      return new MergeableNode<>(node.getName(), null);
    }

    generateAndSaveGraph(node, "average", this::aggregatedDataToAverageWeightedObservedPoint);
    generateAndSaveGraph(node, "count", this::aggregatedDataToCountWeightedObservedPoint);

    return new MergeableNode<>(node.getName(), node.getData());
  }

  private void generateAndSaveGraph(MergeableNode<AggregatedMeasurement> node, String label, Function<Map.Entry<Long, Measurement>, WeightedObservedPoint> aggregatedDataToWeightedObservedPointFunction) {
    Collection<WeightedObservedPoint> observedPoints = getObservedPoints(node, aggregatedDataToWeightedObservedPointFunction);

    LOGGER.debug("");
    LOGGER.debug("---");
    LOGGER.debug(node.getName());
    LOGGER.debug("---");

    long maxN = 262144;
    double maxT = Double.NEGATIVE_INFINITY;

    XYSeriesCollection dataset = new XYSeriesCollection();

    XYSeries actualSeries = new XYSeries("Actual");
    for (WeightedObservedPoint observedPoint : observedPoints) {
      double t = observedPoint.getY();
      if(t > maxT) {
        maxT = t;
      }

      actualSeries.add(observedPoint.getX(), t);
    }
    dataset.addSeries(actualSeries);

    for (FittingFunctionFinder fittingFunctionFinder : FITTING_FUNCTION_FINDER_LIST) {
      Optional<FittingFunction> currentFittingFunctionOptional = fittingFunctionFinder.findFittingFunction(observedPoints);

      if(currentFittingFunctionOptional.isPresent()) {
        FittingFunction currentFittingFunction = currentFittingFunctionOptional.get();
        XYSeries currentSeries = new XYSeries(currentFittingFunction.getClass().getSimpleName());
        for (double n = 0L; n < maxN; n += maxN / 200.0d) {
          double t = currentFittingFunction.f(n);
//          if (t > maxT) {
//            maxT = t;
//          }

          currentSeries.add(n, t);
        }
        dataset.addSeries(currentSeries);

        LOGGER.debug(fittingFunctionFinder.getName() + ";" + currentFittingFunction.getRms() + ";" + currentFittingFunction.toString());
      } else {
        LOGGER.debug("N/A");
      }
    }

    try {
      saveGraph(node.getName(), label, (long) (maxN * 1.05d), maxT * 1.05d, dataset);
    } catch (Exception e) {
      e.printStackTrace();
    }

    LOGGER.debug("---");
  }

  private Collection<WeightedObservedPoint> getObservedPoints(MergeableNode<AggregatedMeasurement> node, Function<Map.Entry<Long, Measurement>, WeightedObservedPoint> aggregatedDataToWeightedObservedPointFunction) {
    final List<WeightedObservedPoint> observedPoints = new ArrayList<>();
    for (Map.Entry<Long, Measurement> aggregatedData : node.getData().getAggregatedData().entrySet()) {
      observedPoints.add(aggregatedDataToWeightedObservedPointFunction.apply(aggregatedData));
    }

    return observedPoints;
  }

  private WeightedObservedPoint aggregatedDataToAverageWeightedObservedPoint(Map.Entry<Long, Measurement> aggregatedData) {
    return new WeightedObservedPoint(1.0d, aggregatedData.getKey(), aggregatedData.getValue().getTotal() / aggregatedData.getValue().getCount());
  }

  private WeightedObservedPoint aggregatedDataToCountWeightedObservedPoint(Map.Entry<Long, Measurement> aggregatedData) {
    return new WeightedObservedPoint(1.0d, aggregatedData.getKey(), aggregatedData.getValue().getCount());
  }

  private void saveGraph(String name, String label, long maxN, double maxT, XYSeriesCollection dataset) throws Exception {
    JFreeChart chart = ChartFactory.createXYLineChart(
        null,                     // chart title
        "n",                      // x axis label
        "t",                      // y axis label
        dataset,                  // data
        PlotOrientation.VERTICAL, // plot orientation
        true,                     // include legend
        false,                    // tooltips
        false                    // urls
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

    // Actual data
    renderer.setSeriesPaint(0, BLACK);
    renderer.setSeriesLinesVisible(0, false);
    renderer.setSeriesShapesVisible(0, true);

    // Fitting functions
    renderer.setBaseLinesVisible(true);
    renderer.setBaseShapesVisible(false);
    renderer.setAutoPopulateSeriesStroke(true);
    renderer.setSeriesPaint(1, GREEN);
    renderer.setSeriesPaint(2, BLUE);
    renderer.setSeriesPaint(3, RED);
    renderer.setSeriesPaint(4, YELLOW);
    renderer.setSeriesPaint(5, PINK);
    renderer.setSeriesPaint(6, CYAN);
    renderer.setSeriesPaint(7, ORANGE);

    plot.setRenderer(renderer);

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setRange(0.0d, maxT);
    rangeAxis.setTickUnit(new NumberTickUnit(Math.round(maxT / 8.0d)));

    NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
    domainAxis.setRange(0.0d, maxN);
    domainAxis.setTickUnit(new NumberTickUnit(Math.round(maxN / 8.0d)));

    BufferedImage bufferedImage = chart.createBufferedImage(1200, 675);
    File outputFile = new File(name + "-" + label + "-" + System.nanoTime() + "-chart.png");
    ImageIO.write(bufferedImage, "png", outputFile);
  }
}
