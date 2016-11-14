package tech.dario.dissertation.timerecorder.tree;

public class Tree<T extends MergeableValue<T>> extends Node<T> {

  public Tree() {
    super("root");
  }
}
