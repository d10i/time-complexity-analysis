package tech.dario.timecomplexityanalysis.timerecorder.tree;

import java.util.ArrayDeque;
import java.util.Collection;

public class MergeableCollection<T> implements Mergeable<MergeableCollection<T>> {
  private final Collection<T> collection;

  public MergeableCollection(Collection<T> collection) {
    this.collection = new ArrayDeque<>(collection);
  }

  public static <T> MergeableCollection<T> fromElement(T element) {
    Collection<T> collection = new ArrayDeque<>();
    collection.add(element);
    return new MergeableCollection<>(collection);
  }

  public static <T> MergeableCollection<T> empty() {
    return new MergeableCollection<>(new ArrayDeque<>());
  }

  public Collection<T> getCollection() {
    return collection;
  }

  public int size() {
    return collection.size();
  }

  @Override
  public MergeableCollection<T> mergeWith(MergeableCollection<T> otherCollection) {
    Collection<T> newCollection = new ArrayDeque<>(size() + otherCollection.size());
    newCollection.addAll(collection);
    newCollection.addAll(otherCollection.getCollection());
    return new MergeableCollection<>(newCollection);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MergeableCollection)) {
      return false;
    }

    MergeableCollection<?> that = (MergeableCollection<?>) o;

    return collection != null ? collection.equals(that.collection) : that.collection == null;
  }

  @Override
  public int hashCode() {
    return collection != null ? collection.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "MergeableCollection{" +
        "collection=" + collection +
        '}';
  }
}
