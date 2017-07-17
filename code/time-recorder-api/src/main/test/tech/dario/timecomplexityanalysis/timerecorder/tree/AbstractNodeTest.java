package tech.dario.timecomplexityanalysis.timerecorder.tree;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class AbstractNodeTest {
  @Test
  public void testAdd() throws Exception {
    TestIntegerAbstractNode node1 = new TestIntegerAbstractNode("node1");
    TestIntegerAbstractNode node2a = new TestIntegerAbstractNode("node2");
    node1.add(node2a);

    assertEquals(1, node1.getChildren().size());
    assertNull(node1.getParent());

    assertEquals(node2a, node1.getChild("node2"));
    assertEquals(0, node2a.getChildren().size());
    assertEquals(node1, node2a.getParent());

    TestIntegerAbstractNode node2b = new TestIntegerAbstractNode("node2");
    node1.add(node2b);

    assertEquals(1, node1.getChildren().size());
    assertNull(node1.getParent());

    assertEquals(node2b, node1.getChild("node2"));
    assertEquals(0, node2b.getChildren().size());
    assertEquals(node1, node2b.getParent());
  }

  @Test
  public void testMap() throws Exception {
    TestIntegerAbstractNode node1 = new TestIntegerAbstractNode("node1", 1);
    TestIntegerAbstractNode node2a = new TestIntegerAbstractNode("node2a", 2);
    TestIntegerAbstractNode node2b = new TestIntegerAbstractNode("node2b", 3);
    TestIntegerAbstractNode node3a = new TestIntegerAbstractNode("node3a", 4);

    /*
     *
     *       1
     *      / \
     *    2a  2b
     *   /
     * 3a
     *
     */
    node2a.add(node3a);
    node1.add(node2a);
    node1.add(node2b);

    TestFloatAbstractNode newNode1 = node1.map(testIntegerAbstractNode ->
        new TestFloatAbstractNode(testIntegerAbstractNode.getName().toUpperCase(), testIntegerAbstractNode.getData() * 2.0f)
    );

    TestFloatAbstractNode newNode2a = newNode1.getChild("NODE2A");
    TestFloatAbstractNode newNode2b = newNode1.getChild("NODE2B");
    TestFloatAbstractNode newNode3a = newNode2a.getChild("NODE3A");

    assertEquals("NODE1", newNode1.getName());
    assertEquals(2.0f, newNode1.getData(), 0.00000001f);
    assertEquals(2, newNode1.getChildren().size());
    assertNull(newNode1.getParent());

    assertEquals("NODE2A", newNode2a.getName());
    assertEquals(4.0f, newNode2a.getData(), 0.00000001f);
    assertEquals(1, newNode2a.getChildren().size());
    assertEquals(newNode1, newNode2a.getParent());

    assertEquals("NODE2B", newNode2b.getName());
    assertEquals(6.0f, newNode2b.getData(), 0.00000001f);
    assertEquals(0, newNode2b.getChildren().size());
    assertEquals(newNode1, newNode2b.getParent());

    assertEquals("NODE3A", newNode3a.getName());
    assertEquals(8.0f, newNode3a.getData(), 0.00000001f);
    assertEquals(0, newNode3a.getChildren().size());
    assertEquals(newNode2a, newNode3a.getParent());
  }

  private class TestIntegerAbstractNode extends AbstractNode<Integer, TestIntegerAbstractNode> {
    TestIntegerAbstractNode(String name) {
      super(name);
    }

    TestIntegerAbstractNode(String name, int data) {
      super(name, data);
    }
  }

  private class TestFloatAbstractNode extends AbstractNode<Float, TestFloatAbstractNode> {
    TestFloatAbstractNode(String name) {
      super(name);
    }

    TestFloatAbstractNode(String name, float data) {
      super(name, data);
    }
  }
}
