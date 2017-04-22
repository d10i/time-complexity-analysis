package tech.dario.timecomplexityanalysis.timerecorder.tree;

public class SimpleTree<T> extends AbstractTree<T, SimpleNode<T>, SimpleTree<T>> {

  public SimpleTree() {
    this(new SimpleNode<>("root", null));
  }

  public SimpleTree(SimpleNode<T> rootNode) {
    super(rootNode);
  }
}
