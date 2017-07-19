package tech.dario.timecomplexityanalysis.sdk.domain;

import java.util.Iterator;
import tech.dario.timecomplexityanalysis.sdk.mappers.AggregatedMeasurementNodeAnalyser;
import tech.dario.timecomplexityanalysis.sdk.mappers.AggregatedMeasurementNodeDebugger;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleTree;

public class InterpolatedTree extends SimpleTree<InterpolatedMeasurement> {

  private InterpolatedTree(final SimpleNode<InterpolatedMeasurement> rootNode) {
    super(rootNode);
  }

  public static InterpolatedTree fromAggregatedMeasurement(final MergeableTree<AggregatedMeasurement> aggregatedTree, final Probe probe) {
    // TODO: comment out next line!
    aggregatedTree.map(MergeableTree::new, new AggregatedMeasurementNodeDebugger());
    final SimpleTree<InterpolatedMeasurement> interpolatedTree = aggregatedTree.map(SimpleTree::new, new AggregatedMeasurementNodeAnalyser<>(probe));
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

  @Override
  public String toString() {
    return toString(getRootNode());
  }

  public String toString(final SimpleNode<InterpolatedMeasurement> node) {
    String[] childrenFunctionStrings = new String[node.getChildren().size()];
    Iterator<SimpleNode<InterpolatedMeasurement>> iterator = node.getChildren().values().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      childrenFunctionStrings[i] = toString(iterator.next());
    }

    String allChildrenFunctionString = String.join(" + ", childrenFunctionStrings);

    final InterpolatedMeasurement nodeData = node.getData();
    if(nodeData == null) {
      // Root node
      return allChildrenFunctionString;
    }

    final String thisNodeCountFunctionString = nodeData.getCountFunction().toString();
    final String thisNodeAverageFunctionString = nodeData.getAverageFunction().toString();
    return "(" + thisNodeCountFunctionString + ") * (" + thisNodeAverageFunctionString + " + (" + allChildrenFunctionString + "))";
  }
}
