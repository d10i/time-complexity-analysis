package tech.dario.timecomplexityanalysis.timerecorder.tree;

import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class MergeableNodeTest {
  @Test
  public void testAddData() throws Exception {
    MergeableNode<MergeableInteger> mergeableNode1 = new MergeableNode<>("node1", new MergeableInteger(100));
    mergeableNode1.addData(new MergeableInteger(200));

    assertEquals(300, mergeableNode1.getData().value);
  }

  @Test
  public void testMergeWith() throws Exception {
    /*
     * 1:
     *     A
     *    / \
     *   B   C
     *  /
     * D
     *
     */
    MergeableNode<MergeableInteger> mergeableNodeA1 = new MergeableNode<>("nodeA", new MergeableInteger(1));
    MergeableNode<MergeableInteger> mergeableNodeB1 = new MergeableNode<>("nodeB", new MergeableInteger(2));
    MergeableNode<MergeableInteger> mergeableNodeC1 = new MergeableNode<>("nodeC", new MergeableInteger(3));
    MergeableNode<MergeableInteger> mergeableNodeD1 = new MergeableNode<>("nodeD", new MergeableInteger(4));
    mergeableNodeB1.add(mergeableNodeD1);
    mergeableNodeA1.add(mergeableNodeB1);
    mergeableNodeA1.add(mergeableNodeC1);

    /*
     * 2:
     *     A
     *      \
     *       C
     *        \
     *         E
     *
     */
    MergeableNode<MergeableInteger> mergeableNodeA2 = new MergeableNode<>("nodeA", new MergeableInteger(10));
    MergeableNode<MergeableInteger> mergeableNodeC2 = new MergeableNode<>("nodeC", new MergeableInteger(20));
    MergeableNode<MergeableInteger> mergeableNodeE2 = new MergeableNode<>("nodeE", new MergeableInteger(30));
    mergeableNodeC2.add(mergeableNodeE2);
    mergeableNodeA2.add(mergeableNodeC2);

    /*
     * 3 (expected):
     *     A
     *    / \
     *   B   C
     *  /     \
     * D       E
     *
     */
    MergeableNode<MergeableInteger> mergeableNodeA3 = mergeableNodeA1.mergeWith(mergeableNodeA2);
    MergeableNode<MergeableInteger> mergeableNodeB3 = mergeableNodeA3.getChild("nodeB");
    MergeableNode<MergeableInteger> mergeableNodeC3 = mergeableNodeA3.getChild("nodeC");
    MergeableNode<MergeableInteger> mergeableNodeD3 = mergeableNodeB3.getChild("nodeD");
    MergeableNode<MergeableInteger> mergeableNodeE3 = mergeableNodeC3.getChild("nodeE");

    assertEquals("nodeA", mergeableNodeA3.getName());
    assertEquals(11, mergeableNodeA3.getData().value);
    assertEquals(2, mergeableNodeA3.getChildren().size());
    assertNull(mergeableNodeA3.getParent());

    assertEquals("nodeB", mergeableNodeB3.getName());
    assertEquals(2, mergeableNodeB3.getData().value);
    assertEquals(1, mergeableNodeB3.getChildren().size());
    assertEquals(mergeableNodeA3, mergeableNodeB3.getParent());

    assertEquals("nodeC", mergeableNodeC3.getName());
    assertEquals(23, mergeableNodeC3.getData().value);
    assertEquals(1, mergeableNodeC3.getChildren().size());
    assertEquals(mergeableNodeA3, mergeableNodeC3.getParent());

    assertEquals("nodeD", mergeableNodeD3.getName());
    assertEquals(4, mergeableNodeD3.getData().value);
    assertEquals(0, mergeableNodeD3.getChildren().size());
    assertEquals(mergeableNodeB3, mergeableNodeD3.getParent());

    assertEquals("nodeE", mergeableNodeE3.getName());
    assertEquals(30, mergeableNodeE3.getData().value);
    assertEquals(0, mergeableNodeE3.getChildren().size());
    assertEquals(mergeableNodeC3, mergeableNodeE3.getParent());
  }

  private class MergeableInteger implements Mergeable<MergeableInteger> {
    private final int value;

    private MergeableInteger(int value) {
      this.value = value;
    }

    @Override
    public MergeableInteger mergeWith(MergeableInteger that) {
      return new MergeableInteger(value + that.value);
    }
  }
}
