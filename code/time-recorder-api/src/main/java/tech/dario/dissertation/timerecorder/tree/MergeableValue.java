package tech.dario.dissertation.timerecorder.tree;

public interface MergeableValue<T extends MergeableValue> {
  T mergeWith(T mergeableValue);
}
