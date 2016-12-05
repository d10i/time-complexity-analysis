package tech.dario.dissertation.timerecorder.tree;

import java.util.LinkedList;
import java.util.Queue;

public class MetricsTree extends MergeableTree<Metrics> {
  public MetricsTree normalise() {
    Queue<NodesPair<Metrics>> queue = new LinkedList<>();

    MetricsTree newTree = new MetricsTree();
    queue.add(new NodesPair<>(this, newTree));

    while (!queue.isEmpty()) {
      final NodesPair<Metrics> nodes = queue.remove();

      final MergeableNode<Metrics> node = nodes.getNode1();
      final MergeableNode<Metrics> newNode = nodes.getNode2();

      for (MergeableNode<Metrics> childNode : node.getChildren().values()) {
        MergeableNode<Metrics> newChildNode = new MergeableNode<>(childNode.getName(), newMetricsMinusChildren(node, childNode));
        newNode.add(newChildNode);
        queue.add(new NodesPair<>(childNode, newChildNode));
      }
    }

    return newTree;
  }

  // TODO: probably shouldn't live here
  private Metrics newMetricsMinusChildren(final MergeableNode<Metrics> node, final MergeableNode<Metrics> childNode) {
    double total = childNode.getData().getTotal();

    for(MergeableNode<Metrics> child: childNode.getChildren().values()) {
      total -= child.getData().getTotal();
    }

    double countMultiplier = node.getData() != null ? node.getData().getCount() : 1.0d;

    return new Metrics(childNode.getData().getCount() / countMultiplier, total / countMultiplier);
  }

  private static class NodesPair<T extends MergeableValue<T>> {
    private final MergeableNode<T> node1;
    private final MergeableNode<T> node2;

    public NodesPair(MergeableNode<T> node1, MergeableNode<T> node2) {
      this.node1 = node1;
      this.node2 = node2;
    }

    public MergeableNode<T> getNode1() {
      return node1;
    }

    public MergeableNode<T> getNode2() {
      return node2;
    }
  }
}
