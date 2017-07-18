package tech.dario.timecomplexityanalysis.timerecorder.tree;

public interface Mergeable<T extends Mergeable<T>> {
  T mergeWith(T mergeable);
}
