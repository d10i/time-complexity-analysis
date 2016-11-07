package tech.dario.dissertation.timerecorder.akka.tree

import java.io._

import org.junit.Assert.assertEquals
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

class TreeScalaTest extends AssertionsForJUnit {

  @Test def testSerialize() {
    val fileName: String = s"tree.ser"
    val initialTree = new Tree[Metrics]
    initialTree.add(Metrics.fromElapsedTime(98237L), MeasuredStackTraceElements.fromStackTrace(Thread.currentThread().getStackTrace))

    // Serialize
    val fileOut = new FileOutputStream(fileName)
    val out = new ObjectOutputStream(fileOut)
    out.writeObject(initialTree)
    out.close()
    fileOut.close()

    // Deserialize
    val fileIn = new FileInputStream(fileName)
    val in = new ObjectInputStream(fileIn)
    val finalTree = in.readObject().asInstanceOf[Tree[Metrics]]
    in.close()
    fileIn.close()

    assertEquals(initialTree, finalTree)
    println(finalTree)
  }
}
