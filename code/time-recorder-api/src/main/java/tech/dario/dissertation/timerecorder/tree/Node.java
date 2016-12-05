package tech.dario.dissertation.timerecorder.tree;

import java.util.HashMap;
import java.util.Map;

public class Node<S, T extends Node<S, T>> {
  private final String name;
  private S data;
  private Map<String, T> children;

  public Node(String name) {
    this(name, null);
  }

  public Node(String name, S data) {
    this.name = name;
    this.data = data;
    this.children = new HashMap<>();
  }

  public T add(T newNode) {
    children.remove(newNode.getName());
    children.put(newNode.getName(), newNode);
    return newNode;
  }

  public String getName() {
    return name;
  }

  public S getData() {
    return data;
  }

  protected void setData(S data) {
    this.data = data;
  }

  public Map<String, T> getChildren() {
    return children;
  }

  protected void setChildren(Map<String, T> children) {
    this.children = children;
  }

  public boolean hasData() {
    return data != null;
  }

  public boolean hasChild(String name) {
    return children.containsKey(name);
  }

  public T getChild(String name) {
    return children.get(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Node<?, ?> node = (Node<?, ?>) o;

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

    for(Node child: children.values()) {
      sb.append(child.toString(indentation + 1));
    }

    return sb.toString();
  }
}
