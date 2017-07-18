package tech.dario.timecomplexityanalysis.sdk.mappers;

import org.junit.Test;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;

import static org.junit.Assert.assertEquals;

public class MeasurementNodeNormaliserTest {
  @Test
  public void testApply() throws Exception {
    // Input tree
    // {root: null}
    // 1a:  {tech.dario.timecomplexityanalysis.testalgorithm.TestAlgorithm.doTask: {count: 1.0, tot: 2,428,897,020.0 ns, avg: 2,428,897,020.0 ns}}
    // 2a:    {tech.dario.timecomplexityanalysis.testalgorithm.Executor1.execute: {count: 1.0, tot: 2,428,821,939.0 ns, avg: 2,428,821,939.0 ns}}
    // 3a:      {tech.dario.timecomplexityanalysis.testalgorithm.Executor2.execute: {count: 50.0, tot: 2,201,664,474.0 ns, avg: 44,033,289.5 ns}}
    // 4a:        {tech.dario.timecomplexityanalysis.testalgorithm.Logarithmic.quick: {count: 50.0, tot: 199,298,528.0 ns, avg: 3,985,970.6 ns}}
    // 4b:        {tech.dario.timecomplexityanalysis.testalgorithm.Executor3.execute: {count: 50000.0, tot: 866,058,915.0 ns, avg: 17,321.2 ns}}
    // 5a:          {tech.dario.timecomplexityanalysis.testalgorithm.Cubic.average: {count: 50000.0, tot: 79,126,330.0 ns, avg: 1,582.5 ns}}
    // 4c:        {tech.dario.timecomplexityanalysis.testalgorithm.Linear.quick: {count: 50.0, tot: 169,917,416.0 ns, avg: 3,398,348.3 ns}}
    // 3b:      {tech.dario.timecomplexityanalysis.testalgorithm.Executor3.execute: {count: 1.0, tot: 19,153.0 ns, avg: 19,153.0 ns}}
    // 4d:        {tech.dario.timecomplexityanalysis.testalgorithm.Cubic.average: {count: 1.0, tot: 3,239.0 ns, avg: 3,239.0 ns}}
    // 3c:      {tech.dario.timecomplexityanalysis.testalgorithm.Quadratic.average: {count: 1.0, tot: 38,722,643.0 ns, avg: 38,722,643.0 ns}}
    // 3d:      {tech.dario.timecomplexityanalysis.testalgorithm.Linearithmic.average: {count: 1.0, tot: 38,795,069.0 ns, avg: 38,795,069.0 ns}}
    // 3e:      {tech.dario.timecomplexityanalysis.testalgorithm.Constant.slow: {count: 1.0, tot: 96,811,459.0 ns, avg: 96,811,459.0 ns}}

    // Input tree
    MergeableTree<Measurement> tree = new MergeableTree<>();
    MergeableNode<Measurement> node1a = new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.TestAlgorithm.doTask", new Measurement(1.0d, 2428897020.0d));
    MergeableNode<Measurement> node2a = node1a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Executor1.execute", new Measurement(1.0d, 2428821939.0d)));
    MergeableNode<Measurement> node3a = node2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Executor2.execute", new Measurement(50.0d, 2201664474.0d)));
    MergeableNode<Measurement> node4a = node3a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Logarithmic.quick", new Measurement(50.0d, 199298528.0d)));
    MergeableNode<Measurement> node4b = node3a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Executor3.execute", new Measurement(50000.0d, 866058915.0d)));
    MergeableNode<Measurement> node5a = node4b.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Cubic.average", new Measurement(50000.0d, 79126330.0d)));
    MergeableNode<Measurement> node4c = node3a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Linear.quick", new Measurement(50.0d, 169917416.0d)));
    MergeableNode<Measurement> node3b = node2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Executor3.execute", new Measurement(1.0, 19153.0d)));
    MergeableNode<Measurement> node4d = node3b.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Cubic.average", new Measurement(1.0, 3239.0d)));
    MergeableNode<Measurement> node3c = node2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Quadratic.average", new Measurement(1.0d, 38722643.0d)));
    MergeableNode<Measurement> node3d = node2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Linearithmic.average", new Measurement(1.0d, 38795069.0d)));
    MergeableNode<Measurement> node3e = node2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Constant.slow", new Measurement(1.0d, 96811459.0d)));
    tree.add(node1a);

    // Expected normalised tree
    MergeableTree<Measurement> normalisedTree = new MergeableTree<>();
    MergeableNode<Measurement> newNode1a = new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.TestAlgorithm.doTask", new Measurement(1.0d, 2428897020.0d - 2428821939.0d));
    MergeableNode<Measurement> newNode2a = newNode1a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Executor1.execute", new Measurement(1.0d, 2428821939.0d - 2201664474.0d - 19153.0d - 38722643.0d - 38795069.0d - 96811459.0d)));
    MergeableNode<Measurement> newNode3a = newNode2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Executor2.execute", new Measurement(50.0d, 2201664474.0d - 199298528.0d - 866058915.0d - 169917416.0d)));
    MergeableNode<Measurement> newNode4a = newNode3a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Logarithmic.quick", new Measurement(1.0d, 199298528.0d / 50.0d)));
    MergeableNode<Measurement> newNode4b = newNode3a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Executor3.execute", new Measurement(1000.0d, (866058915.0d - 79126330.0d) / 50.0d)));
    MergeableNode<Measurement> newNode5a = newNode4b.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Cubic.average", new Measurement(1.0d, 79126330.0d / (50.0d * 1000.0d))));
    MergeableNode<Measurement> newNode4c = newNode3a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Linear.quick", new Measurement(1.0d, 169917416.0d / 50.0d)));
    MergeableNode<Measurement> newNode3b = newNode2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Executor3.execute", new Measurement(1.0, 19153.0d - 3239.0d)));
    MergeableNode<Measurement> newNode4d = newNode3b.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Cubic.average", new Measurement(1.0, 3239.0d)));
    MergeableNode<Measurement> newNode3c = newNode2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Quadratic.average", new Measurement(1.0d, 38722643.0d)));
    MergeableNode<Measurement> newNode3d = newNode2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Linearithmic.average", new Measurement(1.0d, 38795069.0d)));
    MergeableNode<Measurement> newNode3e = newNode2a.add(new MergeableNode<>("tech.dario.timecomplexityanalysis.testalgorithm.Constant.slow", new Measurement(1.0d, 96811459.0d)));
    normalisedTree.add(newNode1a);

    assertEquals("Expected correct normalised tree", normalisedTree, tree.map(MergeableTree::new, new MeasurementNodeNormaliser<>()));
  }
}
