package tech.dario.dissertation.timerecorder.tree;

public class SimpleTree<T> extends AbstractTree<T, SimpleNode<T>> {

  private SimpleNode<T> rootNode;

  public SimpleTree() {
    this(new SimpleNode<>("root", null));
  }

  private SimpleTree(SimpleNode<T> rootNode) {
    this.rootNode = rootNode;
  }

  @Override
  public void add(SimpleNode<T> newChild) {
    rootNode.add(newChild);
  }

  @Override
  public SimpleNode<T> getRootNode() {
    return rootNode;
  }
}
