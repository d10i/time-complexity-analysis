package tech.dario.dissertation.timerecorder.tree;

import java.util.Map;
import java.util.function.Function;

public class MergeableTree<T extends MergeableValue<T>> extends MergeableNode<T> {

  MergeableTree() {
    super("root", null);
  }

  @Override
  public <S extends MergeableValue<S>> MergeableTree<S> cloneWithStrategy(Function<T, S> strategy) {
    MergeableTree<S> newMergeableTree = new MergeableTree<>();

    for (Map.Entry<String, MergeableNode<T>> child : getChildren().entrySet()) {
      MergeableNode<S> newChild = child.getValue().cloneWithStrategy(strategy);
      newMergeableTree.add(newChild);
    }

    return newMergeableTree;
  }
}
