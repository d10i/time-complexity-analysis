package tech.dario.timecomplexityanalysis.timerecorder.tree;

import java.util.ArrayList;
import java.util.List;

public class MergeableList<T> implements Mergeable<MergeableList<T>> {
  private final List<T> list;

  public MergeableList(List<T> list) {
    this.list = list;
  }

  public static <T> MergeableList<T> fromElement(T element) {
    List<T> list = new ArrayList<>();
    list.add(element);
    return new MergeableList<>(list);
  }

  public static <T> MergeableList<T> empty() {
    return new MergeableList<>(new ArrayList<>());
  }

  public List<T> getList() {
    return list;
  }

  public int size() {
    return list.size();
  }

  @Override
  public MergeableList<T> mergeWith(MergeableList<T> otherList) {
    List<T> newList = new ArrayList<>(size() + otherList.size());
    newList.addAll(list);
    newList.addAll(otherList.getList());
    return new MergeableList<>(newList);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MergeableList<T> that = (MergeableList<T>) o;

    return list != null ? list.equals(that.list) : that.list == null;

  }

  @Override
  public int hashCode() {
    return list != null ? list.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "MergeableList[" + list + ']';
  }
}
