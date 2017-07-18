package tech.dario.timecomplexityanalysis.timerecorder.tree;

public class SimpleNode<T> extends AbstractNode<T, SimpleNode<T>> {
  public SimpleNode(final String name) {
    this(name, null);
  }

  public SimpleNode(final String name, final T data) {
    super(name, data);
  }
}
