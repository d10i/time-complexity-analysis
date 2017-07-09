package tech.dario.timecomplexityanalysis.timerecorder.impl.singlethread

import java.util

import org.scalatest.{Matchers, WordSpecLike}
import tech.dario.timecomplexityanalysis.timerecorder.tree.{MergeableNode, MergeableTree, Metrics}

class HelperTest() extends WordSpecLike with Matchers {

  "The ServiceActor" must {
    "fail with method actions missing" in {
      val list = new util.ArrayDeque[MethodAction]()

      list.add(MethodStarted("Method1", 100))
      list.add(MethodStarted("Method2", 200))
      list.add(MethodStarted("Method3", 300))
      list.add(MethodFinished("Method3", 400))
      list.add(MethodFinished("Method2", 500))

      try {
        Helper.methodActionsListToTree(list)
      } catch {
        case iae: IllegalArgumentException => iae.getMessage should be("Finished processing list but current node is not 'root'. Some actions are probably missing from the list.")
      }
    }

    "fail with not-matching method actions" in {
      val list = new util.ArrayDeque[MethodAction]()

      list.add(MethodStarted("Method1", 100))
      list.add(MethodStarted("Method2", 200))
      list.add(MethodStarted("Method3", 300))
      list.add(MethodFinished("Method2", 400))
      list.add(MethodFinished("Method3", 500))
      list.add(MethodFinished("Method1", 600))

      try {
        Helper.methodActionsListToTree(list)
      } catch {
        case iae: IllegalArgumentException => iae.getMessage should be("Received MethodFinished(Method2,400) but was expecting a MethodFinished action with name 'Method3'")
      }
    }

    "tranform the received method actions into a call tree" in {
      val list = new util.ArrayDeque[MethodAction]()

      list.add(MethodStarted("Method1", 100))
      list.add(MethodStarted("Method2", 200))
      list.add(MethodStarted("Method3", 300))
      list.add(MethodFinished("Method3", 400))
      list.add(MethodStarted("Method4", 500))
      list.add(MethodStarted("Method5", 600))
      list.add(MethodFinished("Method5", 700))
      list.add(MethodFinished("Method4", 800))
      list.add(MethodStarted("Method3", 900))
      list.add(MethodFinished("Method3", 1000))
      list.add(MethodStarted("Method5", 1100))
      list.add(MethodFinished("Method5", 1199))
      list.add(MethodFinished("Method2", 1300))
      list.add(MethodFinished("Method1", 1400))


      val expectedTree = new MergeableTree[Metrics]
      val node1a = new MergeableNode[Metrics]("Method1", new Metrics(1.0d, 1300.0d))
      val node2a = node1a.add(new MergeableNode[Metrics]("Method2", new Metrics(1.0d, 1100.0d)))
      val node3a = node2a.add(new MergeableNode[Metrics]("Method3", new Metrics(2.0d, 200.0d)))
      val node3b = node2a.add(new MergeableNode[Metrics]("Method4", new Metrics(1.0d, 300.0d)))
      val node4a = node3b.add(new MergeableNode[Metrics]("Method5", new Metrics(1.0d, 100.0d)))
      val node3c = node2a.add(new MergeableNode[Metrics]("Method5", new Metrics(1.0d, 99.0d)))
      expectedTree.add(node1a)

      val result = Helper.methodActionsListToTree(list)
      result should be(expectedTree)
    }
  }
}
