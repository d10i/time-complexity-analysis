package tech.dario.timecomplexityanalysis.timerecorder.impl.singlethread

import java.util

import scala.annotation.tailrec
import tech.dario.timecomplexityanalysis.timerecorder.tree.{MergeableList, MergeableNode, MergeableTree, Measurement}

object Helper {
  def methodActionsListToTree(list: util.ArrayDeque[MethodAction]): MergeableTree[Measurement] = {
    // Single sorted list to graph with a method actions list in each node
    @tailrec
    def methodActionsListToTreeStep(res: MergeableTree[MergeableList[MethodAction]], currentNode: MergeableNode[MergeableList[MethodAction]], list: List[MethodAction]): MergeableTree[MergeableList[MethodAction]] = {
      list match {
        case Nil =>
          if (currentNode.getName != "root") {
            throw new IllegalArgumentException(s"Finished processing list but current node is not 'root'. Some actions are probably missing from the list.")
          } else {
            res
          }
        case (head@MethodStarted(methodLongName, _)) :: tail =>
          val newElement: MergeableList[MethodAction] = MergeableList.fromElement(head)
          if (currentNode.hasChild(methodLongName)) {
            val childNode = currentNode.getChild(methodLongName)
            childNode.addData(newElement)
            methodActionsListToTreeStep(res, childNode, tail)
          } else {
            val newNode = new MergeableNode(methodLongName, newElement)
            val childNode = currentNode.add(newNode)
            methodActionsListToTreeStep(res, childNode, tail)
          }
        case (head@MethodFinished(methodLongName, _)) :: tail =>
          if (currentNode.getName != methodLongName) {
            throw new IllegalArgumentException(s"Received $head but was expecting a MethodFinished action with name '${currentNode.getName}'")
          }

          currentNode.addData(MergeableList.fromElement(head))

          methodActionsListToTreeStep(res, currentNode.getParent, tail)
      }
    }

    import collection.JavaConverters._
    val tree = new MergeableTree[MergeableList[MethodAction]]
    methodActionsListToTreeStep(tree, tree.getRootNode, list.asScala.toList)

    // Transform each node data from a method actions list to Measurement
    val mergeableMeasurementTree = tree.map[Measurement, MergeableNode[Measurement], MergeableTree[Measurement]](
      (rootNode: MergeableNode[Measurement]) => new MergeableTree[Measurement](rootNode),
      (node: MergeableNode[MergeableList[MethodAction]]) => {
        if (node.getData == null) {
          new MergeableNode[Measurement](node.getName, null)
        } else {
          @tailrec
          def methodActionsListToMeasurementStep(res: Measurement, lastStartTime: Option[Long], list: List[MethodAction]): Measurement = {
            list match {
              case Nil =>
                if (lastStartTime.isDefined) {
                  throw new IllegalArgumentException(s"Found a MethodStarted action without a matching MethodFinished. lastStartTime: ${lastStartTime.get}")
                } else {
                  res
                }
              case (head@MethodStarted(_, nanoTime)) :: tail =>
                if (lastStartTime.isDefined) {
                  throw new IllegalArgumentException(s"Found two consecutive MethodStarted actions")
                } else {
                  methodActionsListToMeasurementStep(res, Some(nanoTime), tail)
                }
              case (head@MethodFinished(_, nanoTime)) :: tail =>
                if (lastStartTime.isEmpty) {
                  throw new IllegalArgumentException(s"Found two consecutive MethodFinished actions")
                } else {
                  methodActionsListToMeasurementStep(res.mergeWith(Measurement.fromElapsedTime(nanoTime - lastStartTime.get)), None, tail)
                }
            }
          }

          val measurement = methodActionsListToMeasurementStep(new Measurement(0.0d, 0.0d), None, node.getData.getList.asScala.toList)
          new MergeableNode[Measurement](node.getName, measurement)
        }
      })

    mergeableMeasurementTree
  }
}
