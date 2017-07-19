package tech.dario.timecomplexityanalysis.sdk.mappers;

import java.util.Collection;
import org.junit.Test;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableCollection;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MeasurementCollectionNodeCleanerTest {
  @Test
  public void testApply() throws Exception {
    // Input tree
    // Data from: http://www.itl.nist.gov/div898/handbook/eda/section3/eda35h3.htm
    final Collection<Measurement> collection = Arrays.asList(
        new Measurement(1.0d, -250000.0d), new Measurement(1.0d, 0680000.0d), new Measurement(1.0d, 0940000.0d), new Measurement(1.0d, 1150000.0d),
        new Measurement(1.0d, 1200000.0d), new Measurement(1.0d, 1260000.0d), new Measurement(1.0d, 1260000.0d), new Measurement(1.0d, 1340000.0d),
        new Measurement(1.0d, 1380000.0d), new Measurement(1.0d, 1430000.0d), new Measurement(1.0d, 1490000.0d), new Measurement(1.0d, 1490000.0d),
        new Measurement(1.0d, 1550000.0d), new Measurement(1.0d, 1560000.0d), new Measurement(1.0d, 1580000.0d), new Measurement(1.0d, 1650000.0d),
        new Measurement(1.0d, 1690000.0d), new Measurement(1.0d, 1700000.0d), new Measurement(1.0d, 1760000.0d), new Measurement(1.0d, 1770000.0d),
        new Measurement(1.0d, 1810000.0d), new Measurement(1.0d, 1910000.0d), new Measurement(1.0d, 1940000.0d), new Measurement(1.0d, 1960000.0d),
        new Measurement(1.0d, 1990000.0d), new Measurement(1.0d, 2060000.0d), new Measurement(1.0d, 2090000.0d), new Measurement(1.0d, 2100000.0d),
        new Measurement(1.0d, 2140000.0d), new Measurement(1.0d, 2150000.0d), new Measurement(1.0d, 2230000.0d), new Measurement(1.0d, 2240000.0d),
        new Measurement(1.0d, 2260000.0d), new Measurement(1.0d, 2350000.0d), new Measurement(1.0d, 2370000.0d), new Measurement(1.0d, 2400000.0d),
        new Measurement(1.0d, 2470000.0d), new Measurement(1.0d, 2540000.0d), new Measurement(1.0d, 2620000.0d), new Measurement(1.0d, 2640000.0d),
        new Measurement(1.0d, 2900000.0d), new Measurement(1.0d, 2920000.0d), new Measurement(1.0d, 2920000.0d), new Measurement(1.0d, 2930000.0d),
        new Measurement(1.0d, 3210000.0d), new Measurement(1.0d, 3260000.0d), new Measurement(1.0d, 3300000.0d), new Measurement(1.0d, 3590000.0d),
        new Measurement(1.0d, 3680000.0d), new Measurement(1.0d, 4300000.0d), new Measurement(1.0d, 4640000.0d), new Measurement(1.0d, 5340000.0d),
        new Measurement(1.0d, 5420000.0d), new Measurement(1.0d, 6010000.0d)
    );

    final Collection<Measurement> expected = Arrays.asList(
        new Measurement(1.0d, -250000.0d), new Measurement(1.0d, 0680000.0d), new Measurement(1.0d, 0940000.0d), new Measurement(1.0d, 1150000.0d),
        new Measurement(1.0d, 1200000.0d), new Measurement(1.0d, 1260000.0d), new Measurement(1.0d, 1260000.0d), new Measurement(1.0d, 1340000.0d),
        new Measurement(1.0d, 1380000.0d), new Measurement(1.0d, 1430000.0d), new Measurement(1.0d, 1490000.0d), new Measurement(1.0d, 1490000.0d),
        new Measurement(1.0d, 1550000.0d), new Measurement(1.0d, 1560000.0d), new Measurement(1.0d, 1580000.0d), new Measurement(1.0d, 1650000.0d),
        new Measurement(1.0d, 1690000.0d), new Measurement(1.0d, 1700000.0d), new Measurement(1.0d, 1760000.0d), new Measurement(1.0d, 1770000.0d),
        new Measurement(1.0d, 1810000.0d), new Measurement(1.0d, 1910000.0d), new Measurement(1.0d, 1940000.0d), new Measurement(1.0d, 1960000.0d),
        new Measurement(1.0d, 1990000.0d), new Measurement(1.0d, 2060000.0d), new Measurement(1.0d, 2090000.0d), new Measurement(1.0d, 2100000.0d),
        new Measurement(1.0d, 2140000.0d), new Measurement(1.0d, 2150000.0d), new Measurement(1.0d, 2230000.0d), new Measurement(1.0d, 2240000.0d),
        new Measurement(1.0d, 2260000.0d), new Measurement(1.0d, 2350000.0d), new Measurement(1.0d, 2370000.0d), new Measurement(1.0d, 2400000.0d),
        new Measurement(1.0d, 2470000.0d), new Measurement(1.0d, 2540000.0d), new Measurement(1.0d, 2620000.0d), new Measurement(1.0d, 2640000.0d),
        new Measurement(1.0d, 2900000.0d), new Measurement(1.0d, 2920000.0d), new Measurement(1.0d, 2920000.0d), new Measurement(1.0d, 2930000.0d),
        new Measurement(1.0d, 3210000.0d), new Measurement(1.0d, 3260000.0d), new Measurement(1.0d, 3300000.0d), new Measurement(1.0d, 3590000.0d),
        new Measurement(1.0d, 3680000.0d), new Measurement(1.0d, 4300000.0d), new Measurement(1.0d, 4640000.0d)
    );

    final MergeableCollection<Measurement> measurementCollection = new MergeableCollection<>(collection);
    final MergeableNode<MergeableCollection<Measurement>> measurementCollectionNode = new MergeableNode<>("root", measurementCollection);
    final MergeableTree<MergeableCollection<Measurement>> measurementCollectionTree = new MergeableTree<>(measurementCollectionNode);
    final MergeableCollection<Measurement> actualMeasurement = measurementCollectionTree.map(MergeableTree::new, new MeasurementCollectionNodeCleaner<>(10)).getRootNode().getData();

    assertEquals("Expected correct size", collection.size() - 3, actualMeasurement.getCollection().size(), 0.0000001d);
    assertArrayEquals("Expected correct collection", actualMeasurement.getCollection().toArray(), expected.toArray());
  }
}
