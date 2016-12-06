package tech.dario.dissertation.timerecorder.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

abstract class AbstractTree<T, S extends AbstractNode<T, S>> {

  public abstract void add(S newChild);
  public abstract S getRootNode();

  public <T2, S2 extends AbstractNode<T2, S2>> S2 map(Function<? super S, ? extends S2> mapper) {
    Queue<NodesPair<T, S, T2, S2>> queue = new LinkedList<>();

    S2 newRootNode = mapper.apply(getRootNode());

    queue.add(new NodesPair<>(getRootNode(), newRootNode));

    while (!queue.isEmpty()) {
      final NodesPair<T, S, T2, S2> nodes = queue.remove();

      final S node = nodes.getNode1();
      final S2 newNode = nodes.getNode2();

      for (S childNode : node.getChildren().values()) {
        S2 newChildNode = mapper.apply(childNode);
        newNode.add(newChildNode);
        queue.add(new NodesPair<>(childNode, newChildNode));
      }
    }

    return newRootNode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MergeableTree<?> that = (MergeableTree<?>) o;

    return getRootNode() != null ? getRootNode().equals(that.getRootNode()) : that.getRootNode() == null;

  }

  @Override
  public int hashCode() {
    return getRootNode() != null ? getRootNode().hashCode() : 0;
  }

  @Override
  public String toString() {
    return getRootNode().toString();
  }
}
