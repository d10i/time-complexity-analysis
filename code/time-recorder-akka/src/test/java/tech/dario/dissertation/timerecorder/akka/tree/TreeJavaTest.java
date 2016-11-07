package tech.dario.dissertation.timerecorder.akka.tree;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;

public class TreeJavaTest {
  @Test
  public void testSerialize() throws Exception {
    final String fileName = "tree.ser";
    final Tree<Metrics> initialTree = new Tree<>();
    initialTree.add(Metrics.fromElapsedTime(98237L), MeasuredStackTraceElements.fromStackTrace(Thread.currentThread().getStackTrace()));

    // Serialize
    final FileOutputStream fileOut = new FileOutputStream(fileName);
    final ObjectOutputStream out = new ObjectOutputStream(fileOut);
    out.writeObject(initialTree);
    out.close();
    fileOut.close();

    // Deserialize
    final FileInputStream fileIn = new FileInputStream(fileName);
    final ObjectInputStream in = new ObjectInputStream(fileIn);
    final Tree finalTree = (Tree) in.readObject();
    in.close();
    fileIn.close();

    assertEquals(initialTree, finalTree);
  }
}
