package tech.dario.dissertation.timerecorder.tree;

import java.util.HashMap;
import java.util.Map;

public class Node<T extends MergeableValue<T>> implements MergeableValue<Node<T>> {
  private final String name;
  private T data;
  private Map<String, Node<T>> children;

  public Node(String name) {
    this(name, null);
  }

  public Node(String name, T data) {
    this.name = name;
    this.data = data;
    this.children = new HashMap<>();
  }

  public Node<T> add(String name, T data) {
    Node<T> newNode = new Node<>(name, data);
    children.remove(name);
    children.put(name, newNode);
    return newNode;
  }

  public void add(T data, MeasuredStackTraceElements measuredStackTraceElements) {
    if(measuredStackTraceElements.size() == 0) {
      addData(data);
      return;
    }

    String lastElement = measuredStackTraceElements.getLastElement();
    if(children.containsKey(lastElement)) {
      children.get(lastElement).add(data, measuredStackTraceElements.withLastElementRemoved());
    } else {
      Node<T> newNode = new Node<>(lastElement);
      newNode.add(data, measuredStackTraceElements.withLastElementRemoved());
      children.put(lastElement, newNode);
    }
  }

  public String getName() {
    return name;
  }

  public T getData() {
    return data;
  }

  public boolean hasData() {
    return data != null;
  }

  public Map<String, Node<T>> getChildren() {
    return children;
  }

  public boolean hasChild(String name) {
    return children.containsKey(name);
  }

  public Node<T> getChild(String name) {
    return children.get(name);
  }

  @Override
  public Node<T> mergeWith(Node<T> otherNode) {
    if(!this.name.equals(otherNode.getName())) {
      String message = String.format("Cannot merge Node '%s' with Node '%s' as names are different", this.name, otherNode.getName());
      throw new RuntimeException(message);
    }

    // Merge node
    if(!this.hasData()) {
      this.data = otherNode.getData();
    } else if(otherNode.hasData()) {
      this.data = this.data.mergeWith(otherNode.getData());
    }


    // Merge children
    Map<String, Node<T>> newChildren = new HashMap<>();

    // Add all children that are in this node, merging them with the ones in the other node when they exist
    for(Map.Entry<String, Node<T>> child: this.children.entrySet()) {
      String childName = child.getKey();
      if(otherNode.hasChild(childName)) {
        newChildren.put(childName, child.getValue().mergeWith(otherNode.getChild(childName)));
      } else {
        newChildren.put(childName, child.getValue());
      }
    }

    // Add all children that are in otherNode but not in this node
    for(Map.Entry<String, Node<T>> child: otherNode.getChildren().entrySet()) {
      String childName = child.getKey();
      if(!newChildren.containsKey(childName)) {
        newChildren.put(childName, child.getValue());
      }
    }

    this.children = newChildren;

    return this;
  }

  private void addData(final T data) {
    if (this.data == null) {
      this.data = data;
    } else {
      this.data = this.data.mergeWith(data);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Node<?> node = (Node<?>) o;

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
    sb.append("(").append(name).append(": ").append(dataString).append(")\n");

    for(Node child: children.values()) {
      sb.append(child.toString(indentation + 1));
    }

    return sb.toString();
  }
}
