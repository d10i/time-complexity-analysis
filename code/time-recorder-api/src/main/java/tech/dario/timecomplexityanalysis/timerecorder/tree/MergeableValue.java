package tech.dario.timecomplexityanalysis.timerecorder.tree;

public interface MergeableValue<T extends MergeableValue<T>> {
  T mergeWith(T mergeableValue);
}
