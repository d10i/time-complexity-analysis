package tech.dario.dissertation.timerecorder.tree;

import java.util.function.Function;

public abstract class AbstractTree<T, S extends AbstractNode<T, S>, R extends AbstractTree<T, S, R>> {

  protected S rootNode;

  protected AbstractTree(S rootNode) {
    this.rootNode = rootNode;
  }

  public <T2, S2 extends AbstractNode<T2, S2>, R2 extends AbstractTree<T2, S2, R2>> R2 map(Function<? super S2, ? extends R2> treeCreator, Function<? super S, ? extends S2> nodeMapper) {
    final S2 newRootNote = rootNode.map(nodeMapper);
    return treeCreator.apply(newRootNote);
  }

  public void add(S newChild) {
    rootNode.add(newChild);
  }

  public S getRootNode() {
    return rootNode;
  }

  protected void setRootNode(S rootNode) {
    this.rootNode = rootNode;
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
