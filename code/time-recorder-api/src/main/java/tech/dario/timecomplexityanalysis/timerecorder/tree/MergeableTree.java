package tech.dario.timecomplexityanalysis.timerecorder.tree;

public class MergeableTree<T extends MergeableValue<T>> extends AbstractTree<T, MergeableNode<T>, MergeableTree<T>> implements MergeableValue<MergeableTree<T>> {

  public MergeableTree() {
    this(new MergeableNode<>("root", null));
  }

  public MergeableTree(MergeableNode<T> rootNode) {
    super(rootNode);
  }

  @Override
  public MergeableTree<T> mergeWith(MergeableTree<T> mergeableTree) {
    return new MergeableTree<>(getRootNode().mergeWith(mergeableTree != null ? mergeableTree.getRootNode() : null));
  }

  public void add(T data, MeasuredStackTraceElements measuredStackTraceElements) {
    rootNode.add(data, measuredStackTraceElements);
  }
}
