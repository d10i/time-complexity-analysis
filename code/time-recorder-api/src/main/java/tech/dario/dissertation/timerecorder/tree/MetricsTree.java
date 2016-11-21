package tech.dario.dissertation.timerecorder.tree;

import java.util.LinkedList;
import java.util.Queue;

public class MetricsTree extends Tree<Metrics> {
  public MetricsTree normalise() {
    Queue<NodesPair<Metrics>> queue = new LinkedList<>();

    MetricsTree newTree = new MetricsTree();
    queue.add(new NodesPair<>(this, newTree));

    while (!queue.isEmpty()) {
      final NodesPair<Metrics> nodes = queue.remove();

      final Node<Metrics> node = nodes.getNode1();
      final Node<Metrics> newNode = nodes.getNode2();

      for (Node<Metrics> childNode : node.getChildren().values()) {
        Node<Metrics> newChildNode = newNode.add(childNode.getName(), newMetricsMinusChildren(node, childNode));
        queue.add(new NodesPair<>(childNode, newChildNode));
      }
    }

    return newTree;
  }

  // TODO: probably shouldn't live here
  private Metrics newMetricsMinusChildren(final Node<Metrics> node, final Node<Metrics> childNode) {
    double total = childNode.getData().getTotal();

    for(Node<Metrics> child: childNode.getChildren().values()) {
      total -= child.getData().getTotal();
    }

    double countMultiplier = node.getData() != null ? node.getData().getCount() : 1.0d;

    return new Metrics(childNode.getData().getCount() / countMultiplier, total / countMultiplier);
  }

  private static class NodesPair<T extends MergeableValue<T>> {
    private final Node<T> node1;
    private final Node<T> node2;

    public NodesPair(Node<T> node1, Node<T> node2) {
      this.node1 = node1;
      this.node2 = node2;
    }

    public Node<T> getNode1() {
      return node1;
    }

    public Node<T> getNode2() {
      return node2;
    }
  }
}
