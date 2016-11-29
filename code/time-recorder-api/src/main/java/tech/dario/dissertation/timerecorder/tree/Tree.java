package tech.dario.dissertation.timerecorder.tree;

import java.util.Map;
import java.util.function.Function;

public class Tree<T extends MergeableValue<T>> extends Node<T> {

  Tree() {
    super("root", null);
  }

  @Override
  public <S extends MergeableValue<S>> Tree<S> cloneWithStrategy(Function<T, S> strategy) {
    Tree<S> newTree = new Tree<>();

    for (Map.Entry<String, Node<T>> child : getChildren().entrySet()) {
      Node<S> newChild = child.getValue().cloneWithStrategy(strategy);
      newTree.add(newChild);
    }

    return newTree;
  }
}
