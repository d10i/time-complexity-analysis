package tech.dario.timecomplexityanalysis.timerecorder.tree;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AbstractTreeTest {
  @Test
  public void testAdd() throws Exception {
    TestIntegerAbstractNode rootNode = new TestIntegerAbstractNode("rootNode");
    TestIntegerAbstractTree tree = new TestIntegerAbstractTree(rootNode);
    assertEquals(rootNode, tree.getRootNode());

    TestIntegerAbstractNode node1 = new TestIntegerAbstractNode("node1");
    tree.add(node1);

    assertEquals(1, tree.getRootNode().getChildren().size());
    assertNull(tree.getRootNode().getParent());

    assertEquals(node1, tree.getRootNode().getChild("node1"));
    assertEquals(tree.getRootNode(), node1.getParent());
  }

  @Test
  public void testMap() throws Exception {
    TestIntegerAbstractNode rootNode = new TestIntegerAbstractNode("rootNode", 1);
    TestIntegerAbstractNode node2a = new TestIntegerAbstractNode("node2a", 2);
    TestIntegerAbstractNode node2b = new TestIntegerAbstractNode("node2b", 3);
    TestIntegerAbstractNode node3a = new TestIntegerAbstractNode("node3a", 4);

    TestIntegerAbstractTree tree = new TestIntegerAbstractTree(rootNode);
    /*
     *
     *     root
     *      / \
     *    2a  2b
     *   /
     * 3a
     *
     */
    node2a.add(node3a);
    tree.add(node2a);
    tree.add(node2b);

    TestFloatAbstractTree newTree = tree.map(
        TestFloatAbstractTree::new,
        testIntegerAbstractNode -> new TestFloatAbstractNode(testIntegerAbstractNode.getName().toUpperCase(), testIntegerAbstractNode.getData() * 2.0f)
    );

    TestFloatAbstractNode newNode2a = newTree.getRootNode().getChild("NODE2A");
    TestFloatAbstractNode newNode2b = newTree.getRootNode().getChild("NODE2B");
    TestFloatAbstractNode newNode3a = newNode2a.getChild("NODE3A");

    assertEquals("ROOTNODE", newTree.getRootNode().getName());
    assertEquals(2.0f, newTree.getRootNode().getData(), 0.00000001f);
    assertEquals(2, newTree.getRootNode().getChildren().size());
    assertNull(newTree.getRootNode().getParent());

    assertEquals("NODE2A", newNode2a.getName());
    assertEquals(4.0f, newNode2a.getData(), 0.00000001f);
    assertEquals(1, newNode2a.getChildren().size());
    assertEquals(newTree.getRootNode(), newNode2a.getParent());

    assertEquals("NODE2B", newNode2b.getName());
    assertEquals(6.0f, newNode2b.getData(), 0.00000001f);
    assertEquals(0, newNode2b.getChildren().size());
    assertEquals(newTree.getRootNode(), newNode2b.getParent());

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

  private class TestIntegerAbstractTree extends AbstractTree<Integer, TestIntegerAbstractNode, TestIntegerAbstractTree> {
    TestIntegerAbstractTree(TestIntegerAbstractNode rootNode) {
      super(rootNode);
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

  private class TestFloatAbstractTree extends AbstractTree<Float, TestFloatAbstractNode, TestFloatAbstractTree> {
    TestFloatAbstractTree(TestFloatAbstractNode rootNode) {
      super(rootNode);
    }
  }
}
