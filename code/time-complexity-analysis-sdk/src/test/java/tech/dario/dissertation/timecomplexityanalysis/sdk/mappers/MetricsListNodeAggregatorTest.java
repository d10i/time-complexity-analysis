package tech.dario.dissertation.timecomplexityanalysis.sdk.mappers;

import org.junit.Test;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.AggregatedMetrics;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Probe;
import tech.dario.dissertation.timerecorder.tree.MergeableNode;
import tech.dario.dissertation.timerecorder.tree.MergeableTree;
import tech.dario.dissertation.timerecorder.tree.Metrics;
import tech.dario.dissertation.timerecorder.tree.MetricsList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MetricsListNodeAggregatorTest {
  @Test
  public void testApply() throws Exception {
    final long n = 1;

    // Input tree
    // Data from: http://www.itl.nist.gov/div898/handbook/eda/section3/eda35h3.htm
    final List<Metrics> list = Arrays.asList(
            new Metrics(1.0d, -250000.0d), new Metrics(1.0d, 0680000.0d), new Metrics(1.0d, 0940000.0d), new Metrics(1.0d, 1150000.0d),
            new Metrics(1.0d, 1200000.0d), new Metrics(1.0d, 1260000.0d), new Metrics(1.0d, 1260000.0d), new Metrics(1.0d, 1340000.0d),
            new Metrics(1.0d, 1380000.0d), new Metrics(1.0d, 1430000.0d), new Metrics(1.0d, 1490000.0d), new Metrics(1.0d, 1490000.0d),
            new Metrics(1.0d, 1550000.0d), new Metrics(1.0d, 1560000.0d), new Metrics(1.0d, 1580000.0d), new Metrics(1.0d, 1650000.0d),
            new Metrics(1.0d, 1690000.0d), new Metrics(1.0d, 1700000.0d), new Metrics(1.0d, 1760000.0d), new Metrics(1.0d, 1770000.0d),
            new Metrics(1.0d, 1810000.0d), new Metrics(1.0d, 1910000.0d), new Metrics(1.0d, 1940000.0d), new Metrics(1.0d, 1960000.0d),
            new Metrics(1.0d, 1990000.0d), new Metrics(1.0d, 2060000.0d), new Metrics(1.0d, 2090000.0d), new Metrics(1.0d, 2100000.0d),
            new Metrics(1.0d, 2140000.0d), new Metrics(1.0d, 2150000.0d), new Metrics(1.0d, 2230000.0d), new Metrics(1.0d, 2240000.0d),
            new Metrics(1.0d, 2260000.0d), new Metrics(1.0d, 2350000.0d), new Metrics(1.0d, 2370000.0d), new Metrics(1.0d, 2400000.0d),
            new Metrics(1.0d, 2470000.0d), new Metrics(1.0d, 2540000.0d), new Metrics(1.0d, 2620000.0d), new Metrics(1.0d, 2640000.0d),
            new Metrics(1.0d, 2900000.0d), new Metrics(1.0d, 2920000.0d), new Metrics(1.0d, 2920000.0d), new Metrics(1.0d, 2930000.0d),
            new Metrics(1.0d, 3210000.0d), new Metrics(1.0d, 3260000.0d), new Metrics(1.0d, 3300000.0d), new Metrics(1.0d, 3590000.0d),
            new Metrics(1.0d, 3680000.0d), new Metrics(1.0d, 4300000.0d), new Metrics(1.0d, 4640000.0d), new Metrics(1.0d, 5340000.0d),
            new Metrics(1.0d, 5420000.0d), new Metrics(1.0d, 6010000.0d)
    );

    final MetricsList metricsList = new MetricsList(list);
    final MergeableNode<MetricsList> metricsListNode = new MergeableNode<>("root", metricsList);
    final MergeableTree<MetricsList> metricsListTree = new MergeableTree<>(metricsListNode);
    final Probe probe = buildProbe();
    final Metrics actualMetrics = metricsListTree.map(MergeableTree::new, new MetricsListNodeAggregator<>(n, probe)).getRootNode().getData().getAggregatedData().get(n);

    assertEquals("Expected correct count", 1.0d, actualMetrics.getCount(), 0.0000001d);
    assertEquals("Expected correct total", 2128431.3725490198d, actualMetrics.getTotal(), 0.0000001d);
  }

  private Probe buildProbe() {
    return new Probe() {
      @Override
      public int getNumWarmUpRounds() {
        // Not used in test
        return 0;
      }

      @Override
      public int getNumRecordingRounds() {
        // Not used in test
        return 0;
      }

      @Override
      public int getNumMaxOutliers() {
        return 10;
      }

      @Override
      public Iterator<Long> buildNIterator() {
        // Not used in test
        return null;
      }
    };
  }
}
