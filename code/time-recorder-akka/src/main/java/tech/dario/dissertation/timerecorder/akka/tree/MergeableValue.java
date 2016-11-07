package tech.dario.dissertation.timerecorder.akka.tree;

import java.io.Serializable;

public interface MergeableValue<T extends MergeableValue> extends Serializable {
  T mergeWith(T mergeableValue);
}
