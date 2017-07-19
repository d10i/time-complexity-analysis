package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.model.MethodFinished;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.model.MethodStarted;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;

public class AsyncInMemoryWriterTest {

  private AsyncInMemoryWriter asyncInMemoryWriter;

  @Before
  public void setup() {
    asyncInMemoryWriter = new AsyncInMemoryWriter();
    asyncInMemoryWriter.open();
  }

  @Test
  public void testTransformMethodActionsMissing() throws Exception {
    try {
      asyncInMemoryWriter.add(new MethodStarted("Method1", 100));
      asyncInMemoryWriter.add(new MethodStarted("Method2", 200));
      asyncInMemoryWriter.add(new MethodStarted("Method3", 300));
      asyncInMemoryWriter.add(new MethodFinished("Method3", 400));
      asyncInMemoryWriter.add(new MethodFinished("Method2", 500));

      asyncInMemoryWriter.close();
      fail("Should have thrown exception");
    } catch (IllegalStateException ise) {
      assertEquals(
          "Finished processing list but current node is 'Method1' instead of 'root'. This means some actions are missing from the list.",
          ise.getMessage());
    }
  }

  @Test
  public void testTransformNotMatchingMethodActions() throws Exception {
    try {
      asyncInMemoryWriter.add(new MethodStarted("Method1", 100));
      asyncInMemoryWriter.add(new MethodStarted("Method2", 200));
      asyncInMemoryWriter.add(new MethodStarted("Method3", 300));
      asyncInMemoryWriter.add(new MethodFinished("Method2", 400));
      asyncInMemoryWriter.add(new MethodFinished("Method3", 500));
      asyncInMemoryWriter.add(new MethodFinished("Method1", 600));

      asyncInMemoryWriter.close();
      fail("Should have thrown exception");
    } catch (IllegalStateException ise) {
      assertEquals(
          "Received MethodFinished{methodLongName='Method2', nanoTime=400} but was expecting a MethodFinished action with name 'Method3'",
          ise.getMessage());
    }
  }

  @Test
  public void testTransformSuccess() throws Exception {
    asyncInMemoryWriter.add(new MethodStarted("Method1", 100));
    asyncInMemoryWriter.add(new MethodStarted("Method2", 200));
    asyncInMemoryWriter.add(new MethodStarted("Method3", 300));
    asyncInMemoryWriter.add(new MethodFinished("Method3", 400));
    asyncInMemoryWriter.add(new MethodStarted("Method4", 500));
    asyncInMemoryWriter.add(new MethodStarted("Method1", 600));
    asyncInMemoryWriter.add(new MethodFinished("Method1", 700));
    asyncInMemoryWriter.add(new MethodFinished("Method4", 800));
    asyncInMemoryWriter.add(new MethodStarted("Method3", 900));
    asyncInMemoryWriter.add(new MethodFinished("Method3", 1000));
    asyncInMemoryWriter.add(new MethodStarted("Method1", 1100));
    asyncInMemoryWriter.add(new MethodFinished("Method1", 1199));
    asyncInMemoryWriter.add(new MethodFinished("Method2", 1300));
    asyncInMemoryWriter.add(new MethodFinished("Method1", 1400));

    MergeableTree<Measurement> expectedTree = new MergeableTree<>();
    MergeableNode<Measurement> node1a = new MergeableNode<>("Method1", new Measurement(1.0d, 1300.0d));
    MergeableNode<Measurement> node2a = node1a.add(new MergeableNode<>("Method2", new Measurement(1.0d, 1100.0d)));
    MergeableNode<Measurement> node3a = node2a.add(new MergeableNode<>("Method3", new Measurement(2.0d, 200.0d)));
    MergeableNode<Measurement> node3b = node2a.add(new MergeableNode<>("Method4", new Measurement(1.0d, 300.0d)));
    MergeableNode<Measurement> node4a = node3b.add(new MergeableNode<>("Method1", new Measurement(1.0d, 100.0d)));
    MergeableNode<Measurement> node3c = node2a.add(new MergeableNode<>("Method1", new Measurement(1.0d, 99.0d)));
    expectedTree.add(node1a);

    MergeableTree<Measurement> result = asyncInMemoryWriter.close();
    assertEquals(expectedTree, result);
  }
}
