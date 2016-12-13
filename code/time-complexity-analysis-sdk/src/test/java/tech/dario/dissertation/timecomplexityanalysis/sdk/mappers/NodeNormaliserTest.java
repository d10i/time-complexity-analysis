package tech.dario.dissertation.timecomplexityanalysis.sdk.mappers;

import org.junit.Test;
import tech.dario.dissertation.timerecorder.tree.MergeableNode;
import tech.dario.dissertation.timerecorder.tree.MergeableTree;
import tech.dario.dissertation.timerecorder.tree.Metrics;

import java.util.function.Function;

import static org.junit.Assert.*;

public class NodeNormaliserTest {
  @Test
  public void testApply() throws Exception {
    // Input tree
    // (root: null)
    //   (tech.dario.dissertation.testalgorithm.TestAlgorithm.doTask: [count: 1.0, tot: 67,426,253.1 μs, avg: 67,426,253.1 μs])
    //     (tech.dario.dissertation.testalgorithm.Executor1.execute: [count: 1.0, tot: 67,426,162.0 μs, avg: 67,426,162.0 μs])
    //       (tech.dario.dissertation.testalgorithm.Constant.slow: [count: 1.0, tot: 45.3 μs, avg: 45.3 μs])
    //       (tech.dario.dissertation.testalgorithm.Quadratic.average: [count: 1.0, tot: 863.9 μs, avg: 863.9 μs])
    //       (tech.dario.dissertation.testalgorithm.Executor3.execute: [count: 1.0, tot: 12.3 μs, avg: 12.3 μs])
    //         (tech.dario.dissertation.testalgorithm.Cubic.quick: [count: 1.0, tot: 2.1 μs, avg: 2.1 μs])
    //       (tech.dario.dissertation.testalgorithm.Linearithmic.average: [count: 1.0, tot: 953.1 μs, avg: 953.1 μs])
    //       (tech.dario.dissertation.testalgorithm.Executor2.execute: [count: 32.0, tot: 67,367,148.1 μs, avg: 2,105,223.4 μs])
    //         (tech.dario.dissertation.testalgorithm.Linear.average: [count: 32.0, tot: 6,506.8 μs, avg: 203.3 μs])
    //         (tech.dario.dissertation.testalgorithm.Logarithmic.average: [count: 32.0, tot: 948.6 μs, avg: 29.6 μs])
    //         (tech.dario.dissertation.testalgorithm.Executor3.execute: [count: 960.0, tot: 67,295,324.2 μs, avg: 65,718.1 μs])
    //           (tech.dario.dissertation.testalgorithm.Cubic.quick: [count: 960.0, tot: 67,263,777.1 μs, avg: 65,687.3 μs])

    // Input tree
    MergeableTree<Metrics> tree = new MergeableTree<>();
    MergeableNode<Metrics> node1a = new MergeableNode<>("tech.dario.dissertation.testalgorithm.TestAlgorithm.doTask", new Metrics(1.0d, 67426253.1));
    MergeableNode<Metrics> node2a = node1a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Executor1.execute", new Metrics(1.0d, 67426162.0)));
    MergeableNode<Metrics> node3a = node2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Constant.slow", new Metrics(1.0d, 45.3)));
    MergeableNode<Metrics> node3b = node2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Quadratic.average", new Metrics(1.0d, 863.9)));
    MergeableNode<Metrics> node3c = node2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Executor3.execute", new Metrics(1.0d, 12.3)));
    MergeableNode<Metrics> node4a = node3c.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Cubic.quick", new Metrics(1.0d, 2.1)));
    MergeableNode<Metrics> node3d = node2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Linearithmic.average", new Metrics(1.0d, 953.1)));
    MergeableNode<Metrics> node3e = node2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Executor2.execute", new Metrics(32.0d, 67367148.1)));
    MergeableNode<Metrics> node4b = node3e.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Linear.average", new Metrics(32.0d, 6506.8)));
    MergeableNode<Metrics> node4c = node3e.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Logarithmic.average", new Metrics(32.0d, 948.6)));
    MergeableNode<Metrics> node4d = node3e.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Executor3.execute", new Metrics(960.0d, 67295324.2)));
    MergeableNode<Metrics> node5a = node4d.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Cubic.quick", new Metrics(960.0d, 67263777.1)));
    tree.add(node1a);

    // Expected normalised tree
    MergeableTree<Metrics> normalisedTree = new MergeableTree<>();
    MergeableNode<Metrics> newNode1a = new MergeableNode<>("tech.dario.dissertation.testalgorithm.TestAlgorithm.doTask", new Metrics(1.0d, 67426253.1 - 67426162.0));
    MergeableNode<Metrics> newNode2a = newNode1a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Executor1.execute", new Metrics(1.0d, 67426162.0 - 45.3 - 863.9 - 12.3 - 953.1 - 67367148.1)));
    MergeableNode<Metrics> newNode3a = newNode2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Constant.slow", new Metrics(1.0d, 45.3)));
    MergeableNode<Metrics> newNode3b = newNode2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Quadratic.average", new Metrics(1.0d, 863.9)));
    MergeableNode<Metrics> newNode3c = newNode2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Executor3.execute", new Metrics(1.0d, 12.3 - 2.1)));
    MergeableNode<Metrics> newNode4a = newNode3c.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Cubic.quick", new Metrics(1.0d, 2.1)));
    MergeableNode<Metrics> newNode3d = newNode2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Linearithmic.average", new Metrics(1.0d, 953.1)));
    MergeableNode<Metrics> newNode3e = newNode2a.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Executor2.execute", new Metrics(32.0d, 67367148.1 - 6506.8 - 948.6 - 67295324.2)));
    MergeableNode<Metrics> newNode4b = newNode3e.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Linear.average", new Metrics(1.0d, 6506.8 / 32.0)));
    MergeableNode<Metrics> newNode4c = newNode3e.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Logarithmic.average", new Metrics(1.0d, 948.6 / 32.0)));
    MergeableNode<Metrics> newNode4d = newNode3e.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Executor3.execute", new Metrics(30.0d, (67295324.2 - 67263777.1) / 32.0)));
    MergeableNode<Metrics> newNode5a = newNode4d.add(new MergeableNode<>("tech.dario.dissertation.testalgorithm.Cubic.quick", new Metrics(1.0d, 67263777.1 / 960.0)));
    normalisedTree.add(newNode1a);

    assertEquals("Expected correct normalised tree", normalisedTree, tree.map(MergeableTree::new, new NodeNormaliser<>()));
  }
}
