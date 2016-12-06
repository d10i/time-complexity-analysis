package tech.dario.dissertation.timerecorder.tree;

public class MergeableTree<T extends MergeableValue<T>> extends AbstractTree<T, MergeableNode<T>> implements MergeableValue<MergeableTree<T>> {

  private MergeableNode<T> rootNode;

  public MergeableTree() {
    this(new MergeableNode<>("root", null));
  }

  public MergeableTree(MergeableNode<T> rootNode) {
    this.rootNode = rootNode;
  }

  @Override
  public void add(MergeableNode<T> newChild) {
    rootNode.add(newChild);
  }

  @Override
  public MergeableNode<T> getRootNode() {
    return rootNode;
  }

  @Override
  public MergeableTree<T> mergeWith(MergeableTree<T> mergeableTree) {
    return new MergeableTree<>(getRootNode().mergeWith(mergeableTree != null ? mergeableTree.getRootNode() : null));
  }

  public void add(T data, MeasuredStackTraceElements measuredStackTraceElements) {
    rootNode.add(data, measuredStackTraceElements);
  }
}
