package tech.dario.timecomplexityanalysis.sdk.domain;

import tech.dario.timecomplexityanalysis.sdk.mappers.AggregatedMeasurementNodeAnalyser;
import tech.dario.timecomplexityanalysis.sdk.mappers.AggregatedMeasurementNodeDebugger;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleTree;

public class InterpolatedTree extends SimpleTree<InterpolatedMeasurement> {

  private InterpolatedTree(final SimpleNode<InterpolatedMeasurement> rootNode) {
    super(rootNode);
  }

  public static InterpolatedTree fromAggregatedMeasurement(final MergeableTree<AggregatedMeasurement> aggregatedTree) {
    // aggregatedTree.map(MergeableTree::new, new AggregatedMeasurementNodeDebugger<>());
    final SimpleTree<InterpolatedMeasurement> interpolatedTree = aggregatedTree.map(SimpleTree::new, new AggregatedMeasurementNodeAnalyser<>());
    return new InterpolatedTree(interpolatedTree.getRootNode());
  }

  public double calculate(final long n) {
    return calculate(n, getRootNode());
  }

  private double calculate(final long n, final SimpleNode<InterpolatedMeasurement> node) {
    double childrenSum = 0.0d;
    for (final SimpleNode<InterpolatedMeasurement> childNode : node.getChildren().values()) {
      childrenSum += calculate(n, childNode);
    }

    final InterpolatedMeasurement nodeData = node.getData();
    if(nodeData == null) {
      // Root node
      return childrenSum;
    }

    final double thisNodeCount = nodeData.getCountFunction().f(n);
    final double thisNodeAverage = nodeData.getAverageFunction().f(n);
    return thisNodeCount * (thisNodeAverage + childrenSum);
  }
}
