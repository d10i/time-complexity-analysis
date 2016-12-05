package tech.dario.dissertation.timerecorder.tree;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractNode<T, R extends AbstractNode<T, R>> {
  private final String name;
  private R parent;
  private T data;
  private Map<String, R> children;

  protected AbstractNode(final String name) {
    this(name, null);
  }

  protected AbstractNode(final String name, final T data) {
    this.name = name;
    this.data = data;
    this.children = new HashMap<>();
  }

  public R add(R newNode) {
    newNode.setParent((R)this);
    children.remove(newNode.getName());
    children.put(newNode.getName(), newNode);
    return newNode;
  }

  public R getParent() {
    return parent;
  }

  void setParent(R parent) {
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

  public Map<String, R> getChildren() {
    return children;
  }

  public boolean hasData() {
    return data != null;
  }

  public boolean hasChild(String name) {
    return children.containsKey(name);
  }

  public R getChild(String name) {
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
