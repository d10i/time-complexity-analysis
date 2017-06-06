package tech.dario.timecomplexityanalysis.timerecorder.tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;

public abstract class AbstractNode<T, S extends AbstractNode<T, S>> {
  private final String name;
  private S parent;
  private T data;
  private Map<String, S> children;

  protected AbstractNode(final String name) {
    this(name, null);
  }

  protected AbstractNode(final String name, final T data) {
    this.name = name;
    this.data = data;
    this.children = new HashMap<>();
  }

  public S add(S newNode) {
    newNode.setParent((S)this);
    children.remove(newNode.getName());
    children.put(newNode.getName(), newNode);
    return newNode;
  }

  public <T2, S2 extends AbstractNode<T2, S2>> S2 map(Function<? super S, ? extends S2> mapper) {
    // BFS
    Queue<NodesPair<T, S, T2, S2>> queue = new LinkedList<>();

    S2 newRootNode = mapper.apply((S)this);

    queue.add(new NodesPair<>((S)this, newRootNode));

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

  public S getParent() {
    return parent;
  }

  void setParent(S parent) {
    this.parent = parent;
  }

  public String getName() {
    return name;
  }

  public T getData() {
    return data;
  }

  protected void setData(T data) {
    this.data = data;
  }

  public Map<String, S> getChildren() {
    return children;
  }

  public boolean hasData() {
    return data != null;
  }

  public boolean hasChild(String name) {
    return children.containsKey(name);
  }

  public S getChild(String name) {
    return children.get(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AbstractNode<?, ?> node = (AbstractNode<?, ?>) o;

    if (name != null ? !name.equals(node.name) : node.name != null) return false;
    if (data != null ? !data.equals(node.data) : node.data != null) return false;
    return children != null ? children.equals(node.children) : node.children == null;

  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (data != null ? data.hashCode() : 0);
    result = 31 * result + (children != null ? children.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return this.toString(0);
  }

  public String toString(int indentation) {
    StringBuilder sb = new StringBuilder();

    for(int i = 0; i < indentation; i++) {
      sb.append("  ");
    }

    String dataString = data == null ? "null": data.toString();
    sb.append("{").append(name).append(": ").append(dataString).append("}\n");

    for(AbstractNode child: children.values()) {
      sb.append(child.toString(indentation + 1));
    }

    return sb.toString();
  }
}
