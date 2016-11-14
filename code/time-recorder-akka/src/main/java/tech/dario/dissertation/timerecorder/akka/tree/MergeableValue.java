package tech.dario.dissertation.timerecorder.akka.tree;

public interface MergeableValue<T extends MergeableValue> {
  T mergeWith(T mergeableValue);
}
