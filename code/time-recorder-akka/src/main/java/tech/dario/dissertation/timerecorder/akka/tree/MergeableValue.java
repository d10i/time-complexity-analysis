package tech.dario.dissertation.timerecorder.akka.tree;

public interface MergeableValue {
    MergeableValue mergeWith(MergeableValue mergeableValue);
}
