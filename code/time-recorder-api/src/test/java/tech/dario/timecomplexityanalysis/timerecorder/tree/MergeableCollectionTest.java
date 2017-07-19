package tech.dario.timecomplexityanalysis.timerecorder.tree;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MergeableCollectionTest {
  @Test
  public void testMergeWith() throws Exception {
    MergeableCollection<Integer> mergeableCollection1 = MergeableCollection.fromElement(100);
    MergeableCollection<Integer> mergeableCollection2 = MergeableCollection.empty();
    MergeableCollection<Integer> mergeableCollection3 = new MergeableCollection<>(Arrays.asList(1, 2, 3));

    MergeableCollection mergeableCollectionMerge = mergeableCollection1.mergeWith(mergeableCollection2).mergeWith(mergeableCollection3);
    assertArrayEquals(new Integer[]{100, 1, 2, 3}, mergeableCollectionMerge.getCollection().toArray());

    // Verify immutability
    assertArrayEquals(new Integer[]{100}, mergeableCollection1.getCollection().toArray());
    assertArrayEquals(new Integer[]{}, mergeableCollection2.getCollection().toArray());
    assertArrayEquals(new Integer[]{1, 2, 3}, mergeableCollection3.getCollection().toArray());
  }
}
