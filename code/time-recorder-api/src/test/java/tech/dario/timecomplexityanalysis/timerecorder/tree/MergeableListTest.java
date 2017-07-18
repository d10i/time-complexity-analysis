package tech.dario.timecomplexityanalysis.timerecorder.tree;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class MergeableListTest {
  @Test
  public void testMergeWith() throws Exception {
    MergeableList<Integer> mergeableList1 = MergeableList.fromElement(100);
    MergeableList<Integer> mergeableList2 = MergeableList.empty();
    MergeableList<Integer> mergeableList3 = new MergeableList<>(Arrays.asList(1, 2, 3));

    MergeableList mergeableListMerge = mergeableList1.mergeWith(mergeableList2).mergeWith(mergeableList3);
    assertArrayEquals(new Integer[]{100, 1, 2, 3}, mergeableListMerge.getList().toArray());

    // Verify immutability
    assertArrayEquals(new Integer[]{100}, mergeableList1.getList().toArray());
    assertArrayEquals(new Integer[]{}, mergeableList2.getList().toArray());
    assertArrayEquals(new Integer[]{1, 2, 3}, mergeableList3.getList().toArray());
  }
}
