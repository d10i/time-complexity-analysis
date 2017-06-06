package tech.dario.timecomplexityanalysis.sdk.domain;

import tech.dario.timecomplexityanalysis.sdk.mappers.AggregatedMetricsNodeAnalyser;
import tech.dario.timecomplexityanalysis.sdk.mappers.AggregatedMetricsNodeDebugger;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleTree;

public class InterpolatedTree extends SimpleTree<InterpolatedMetrics> {

  private InterpolatedTree(final SimpleNode<InterpolatedMetrics> rootNode) {
    super(rootNode);
  }

  public static InterpolatedTree fromAggregatedMetrics(final MergeableTree<AggregatedMetrics> aggregatedTree) {
    // aggregatedTree.map(MergeableTree::new, new AggregatedMetricsNodeDebugger<>());
    final SimpleTree<InterpolatedMetrics> interpolatedTree = aggregatedTree.map(SimpleTree::new, new AggregatedMetricsNodeAnalyser<>());
    return new InterpolatedTree(interpolatedTree.getRootNode());
  }

  public double calculate(final long n) {
    return calculate(n, getRootNode());
  }

  private double calculate(final long n, final SimpleNode<InterpolatedMetrics> node) {
    double childrenSum = 0.0d;
    for (final SimpleNode<InterpolatedMetrics> childNode : node.getChildren().values()) {
      childrenSum += calculate(n, childNode);
    }

    final InterpolatedMetrics nodeData = node.getData();
    if(nodeData == null) {
      // Root node
      return childrenSum;
    }

    final double thisNodeCount = nodeData.getCountFunction().f(n);
    final double thisNodeAverage = nodeData.getAverageFunction().f(n);
    return thisNodeCount * (thisNodeAverage + childrenSum);
  }
}
