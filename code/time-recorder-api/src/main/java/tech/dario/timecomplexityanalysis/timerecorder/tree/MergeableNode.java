package tech.dario.timecomplexityanalysis.timerecorder.tree;

import java.util.Map;

public class MergeableNode<T extends MergeableValue<T>> extends AbstractNode<T, MergeableNode<T>> implements MergeableValue<MergeableNode<T>> {

  public MergeableNode(final String name) {
    this(name, null);
  }

  public MergeableNode(final String name, final T data) {
    super(name, data);
  }

  public void addData(T data) {
    mergeData(data);
  }

  @Override
  public MergeableNode<T> mergeWith(MergeableNode<T> otherNode) {
    if (otherNode == null) {
      return this;
    }

    if (!getName().equals(otherNode.getName())) {
      String message = String.format("Cannot merge MergeableNode '%s' with MergeableNode '%s' as names are different", getName(), otherNode.getName());
      throw new RuntimeException(message);
    }

    MergeableNode<T> mergedNode = new MergeableNode<>(getName(), getData());

    // Merge node data
    mergedNode.mergeData(otherNode.getData());

    // Add all children that are in this node, merging them with the ones in the other node when they exist
    for (Map.Entry<String, MergeableNode<T>> child : getChildren().entrySet()) {
      String childName = child.getKey();
      if (otherNode.hasChild(childName)) {
        mergedNode.add(child.getValue().mergeWith(otherNode.getChild(childName)));
      } else {
        mergedNode.add(child.getValue());
      }
    }

    // Add all children that are in otherNode but not in this node
    for (Map.Entry<String, MergeableNode<T>> child : otherNode.getChildren().entrySet()) {
      String childName = child.getKey();
      if (!mergedNode.hasChild(childName)) {
        mergedNode.add(child.getValue());
      }
    }

    return mergedNode;
  }

  private void mergeData(final T otherData) {
    if (!this.hasData()) {
      setData(otherData);
    } else if (otherData != null) {
      setData(getData().mergeWith(otherData));
    }
  }
}
