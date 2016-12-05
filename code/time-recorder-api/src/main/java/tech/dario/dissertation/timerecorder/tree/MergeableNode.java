package tech.dario.dissertation.timerecorder.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MergeableNode<T extends MergeableValue<T>> extends Node<T, MergeableNode<T>> implements MergeableValue<MergeableNode<T>> {

  public MergeableNode(String name) {
    this(name, null);
  }

  public MergeableNode(String name, T data) {
    super(name, data);
  }

  public void add(T data, MeasuredStackTraceElements measuredStackTraceElements) {
    if(measuredStackTraceElements.size() == 0) {
      mergeData(data);
      return;
    }

    String lastElement = measuredStackTraceElements.getLastElement();
    if(hasChild(lastElement)) {
      getChild(lastElement).add(data, measuredStackTraceElements.withLastElementRemoved());
    } else {
      MergeableNode<T> newNode = new MergeableNode<>(lastElement);
      newNode.add(data, measuredStackTraceElements.withLastElementRemoved());
      getChildren().put(lastElement, newNode);
    }
  }

  public <S extends MergeableValue<S>> MergeableNode<S> cloneWithStrategy(Function<T, S> strategy) {
    MergeableNode<S> newNode = new MergeableNode<>(getName(), strategy.apply(getData()));

    for(Map.Entry<String, MergeableNode<T>> child: getChildren().entrySet()) {
      MergeableNode<S> newChild = child.getValue().cloneWithStrategy(strategy);
      newNode.add(newChild);
    }

    return newNode;
  }

  @Override
  public MergeableNode<T> mergeWith(MergeableNode<T> otherNode) {
    if(otherNode == null) {
      return this;
    }

    if(!getName().equals(otherNode.getName())) {
      String message = String.format("Cannot merge MergeableNode '%s' with MergeableNode '%s' as names are different", getName(), otherNode.getName());
      throw new RuntimeException(message);
    }

    // Merge node
    mergeData(otherNode.getData());

    // Merge children
    Map<String, MergeableNode<T>> newChildren = new HashMap<>();

    // Add all children that are in this node, merging them with the ones in the other node when they exist
    for(Map.Entry<String, MergeableNode<T>> child: getChildren().entrySet()) {
      String childName = child.getKey();
      if(otherNode.hasChild(childName)) {
        newChildren.put(childName, child.getValue().mergeWith(otherNode.getChild(childName)));
      } else {
        newChildren.put(childName, child.getValue());
      }
    }

    // Add all children that are in otherNode but not in this node
    for(Map.Entry<String, MergeableNode<T>> child: otherNode.getChildren().entrySet()) {
      String childName = child.getKey();
      if(!newChildren.containsKey(childName)) {
        newChildren.put(childName, child.getValue());
      }
    }

    setChildren(newChildren);

    return this;
  }

  private void mergeData(final T otherData) {
    if(!this.hasData()) {
      setData(otherData);
    } else if(otherData != null) {
      setData(getData().mergeWith(otherData));
    }
  }
}
