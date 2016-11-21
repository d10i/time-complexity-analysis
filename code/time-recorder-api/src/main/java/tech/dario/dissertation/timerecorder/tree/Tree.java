package tech.dario.dissertation.timerecorder.tree;

public abstract class Tree<T extends MergeableValue<T>> extends Node<T> {

  protected Tree() {
    super("root", null);
  }
}
