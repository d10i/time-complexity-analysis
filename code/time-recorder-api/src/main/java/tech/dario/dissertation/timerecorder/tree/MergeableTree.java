package tech.dario.dissertation.timerecorder.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

public class MergeableTree<T extends MergeableValue<T>> implements MergeableValue<MergeableTree<T>> {

  private MergeableNode<T> rootNode;

  public MergeableTree() {
    this(new MergeableNode<>("root", null));
  }

  private MergeableTree(MergeableNode<T> rootNode) {
    this.rootNode = rootNode;
  }

  public void add(MergeableNode<T> newChild) {
    rootNode.add(newChild);
  }

  public void add(T data, MeasuredStackTraceElements measuredStackTraceElements) {
    rootNode.add(data, measuredStackTraceElements);
  }

  public MergeableNode<T> getRootNode() {
    return rootNode;
  }

  public <T2 extends MergeableValue<T2>> MergeableTree<T2> flatMap(Function<? super MergeableNode<T>, ? extends MergeableNode<T2>> mapper) {
    Queue<NodesPair<T, MergeableNode<T>, T2, MergeableNode<T2>>> queue = new LinkedList<>();

    MergeableNode<T2> newRootNode = mapper.apply(rootNode);

    queue.add(new NodesPair<>(rootNode, newRootNode));

    while (!queue.isEmpty()) {
      final NodesPair<T, MergeableNode<T>, T2, MergeableNode<T2>> nodes = queue.remove();

      final MergeableNode<T> node = nodes.getNode1();
      final MergeableNode<T2> newNode = nodes.getNode2();

      for (MergeableNode<T> childNode : node.getChildren().values()) {
        MergeableNode<T2> newChildNode = mapper.apply(childNode);
        newNode.add(newChildNode);
        queue.add(new NodesPair<>(childNode, newChildNode));
      }
    }

    return new MergeableTree<>(newRootNode);
  }

  @Override
  public MergeableTree<T> mergeWith(MergeableTree<T> mergeableTree) {
    return new MergeableTree<>(getRootNode().mergeWith(mergeableTree != null ? mergeableTree.getRootNode() : null));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MergeableTree<?> that = (MergeableTree<?>) o;

    return rootNode != null ? rootNode.equals(that.rootNode) : that.rootNode == null;

  }

  @Override
  public int hashCode() {
    return rootNode != null ? rootNode.hashCode() : 0;
  }

  @Override
  public String toString() {
    return rootNode.toString();
  }
}
