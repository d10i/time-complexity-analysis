package tech.dario.dissertation.timerecorder.tree;

public class SimpleTree<T> {

  private SimpleNode<T> rootNode;

  public SimpleTree() {
    this(new SimpleNode<>("root", null));
  }

  private SimpleTree(SimpleNode<T> rootNode) {
    this.rootNode = rootNode;
  }

  public void add(SimpleNode<T> newChild) {
    rootNode.add(newChild);
  }

  public SimpleNode<T> getRootNode() {
    return rootNode;
  }
}
