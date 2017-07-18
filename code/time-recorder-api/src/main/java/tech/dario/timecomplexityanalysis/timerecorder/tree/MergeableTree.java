package tech.dario.timecomplexityanalysis.timerecorder.tree;

public class MergeableTree<T extends Mergeable<T>> extends AbstractTree<T, MergeableNode<T>, MergeableTree<T>> implements Mergeable<MergeableTree<T>> {
  public MergeableTree() {
    this(new MergeableNode<>("root", null));
  }

  public MergeableTree(MergeableNode<T> rootNode) {
    super(rootNode);
  }

  @Override
  public MergeableTree<T> mergeWith(MergeableTree<T> otherTree) {
    MergeableNode<T> otherRootNode = null;
    if (otherTree != null) {
      otherRootNode = otherTree.getRootNode();
    }
    return new MergeableTree<>(getRootNode().mergeWith(otherRootNode));
  }
}
