package tech.dario.dissertation.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.awt.Color.*;
import static org.junit.Assert.assertEquals;

public class CubicFunctionFinderTest {
  @Test
  public void findFittingFunction() throws Exception {
    final CubicFunctionFinder cubicFunctionFinder = new CubicFunctionFinder();
    FittingFunction fittingFunction = cubicFunctionFinder.findFittingFunction(new WeightedObservedPoints() {{
      add(1.0d, f(1.0d));
      add(2.0d, f(2.0d));
      add(5.0d, f(5.0d));
      add(10.0d, f(10.0d));
    }}.toList());

    assertEquals("Expected correct point 1", f(1.0d), fittingFunction.f(1.0d), 0.0001d);
    assertEquals("Expected correct point 2", f(2.0d), fittingFunction.f(2.0d), 0.0001d);
    assertEquals("Expected correct point 3", f(5.0d), fittingFunction.f(5.0d), 0.0001d);
    assertEquals("Expected correct point 4", f(10.0d), fittingFunction.f(10.0d), 0.0001d);
    assertEquals("Expected correct point 5", f(50.0d), fittingFunction.f(50.0d), 0.0001d);
    assertEquals("Expected correct RMS", 0.000d, fittingFunction.getRms(), 0.0001d);
    assertEquals("Expected correct string representation", "2.000000 * n^3 + 6.000000 * n^2 + 1.000000 * n + 8.000000 [rms: 0.000000]", fittingFunction.toString());

    long minN = 0L;
    long maxN = 30L;
    XYSeriesCollection dataset = new XYSeriesCollection();
    XYSeries currentSeries = new XYSeries(fittingFunction.getClass().getSimpleName());
    for(double n = minN; n < maxN; n += (maxN - minN) / 200.0d) {
      currentSeries.add(n, fittingFunction.f(n));
    }
    dataset.addSeries(currentSeries);

    try {
      saveGraph(minN, maxN, dataset);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private double f(double n) {
    return 2.0d * n * n * n + 6.0d * n * n + n + 8.0d;
  }

  private void saveGraph(long minN, long maxN, XYSeriesCollection dataset) throws Exception {
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
    renderer.setBaseLinesVisible(false);
    renderer.setBaseShapesVisible(true);
    renderer.setAutoPopulateSeriesStroke(false);
    renderer.setBaseStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    renderer.setSeriesPaint(0, BLACK);
    renderer.setSeriesPaint(1, GREEN);
    renderer.setSeriesPaint(2, BLUE);
    renderer.setSeriesPaint(3, RED);
    renderer.setSeriesPaint(4, YELLOW);
    renderer.setSeriesPaint(5, PINK);
    renderer.setSeriesPaint(6, CYAN);

    plot.setRenderer(renderer);

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
    rangeAxis.setAutoRangeIncludesZero(true);

    NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
    domainAxis.setRange(minN, maxN);
    domainAxis.setTickUnit(new NumberTickUnit(Math.round((maxN - minN) / 8.0d)));

    BufferedImage bufferedImage = chart.createBufferedImage(800, 600);
    File outputFile = new File("chart.png");
    ImageIO.write(bufferedImage, "png", outputFile);
  }
}
