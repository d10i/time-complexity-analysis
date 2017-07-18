package tech.dario.timecomplexityanalysis.timerecorder.impl.akka

import akka.actor._
import akka.pattern.{ask, pipe}
import akka.routing._
import akka.util.Timeout
import tech.dario.timecomplexityanalysis.timerecorder.tree.{MergeableList, MergeableNode, MergeableTree, Measurement}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class ServiceActor extends Actor with ActorLogging {
  private final val NumRoutees = 8 // TODO? num cores - 2
  private final implicit val SaveTimeout = Timeout(60 seconds)

  private val routees = Vector.fill(NumRoutees) {
    val r = context.actorOf(Props[Worker])
    context watch r
    ActorRefRoutee(r)
  }

  private var router = {
    Router(SmallestMailboxRoutingLogic(), routees)
  }

  override def postStop = {
    // log.info(s"$self: Router stopped")
  }

  override def receive = {
    case methodActions: MethodActions =>
      //println(s"Received ${methodActionsArray.toList}")
      router.route(methodActions, sender())

    case s: Save =>
      log.debug("Received Save message")
      val listFutures = routees map (routee => (routee.ref ? s).mapTo[List[MethodAction]])

      // Multiple sorted lists to single sorted list
      val mergedListFuture = Future.reduceLeft(listFutures) {
        case (ma1: List[MethodAction], ma2: List[MethodAction]) =>
          @tailrec
          def mergeSortedListsStep(res: List[MethodAction], a: List[MethodAction], b: List[MethodAction]): List[MethodAction] = {
            (a, b) match {
              case (Nil, Nil) => res
              case (_, Nil) => a.reverse ++ res
              case (Nil, _) => b.reverse ++ res
              case (aHead :: aTail, bHead :: bTail) =>
                if (aHead.nanoTime <= bHead.nanoTime) {
                  mergeSortedListsStep(aHead +: res, aTail, b)
                } else {
                  mergeSortedListsStep(bHead +: res, a, bTail)
                }
            }
          }

          mergeSortedListsStep(List(), ma1, ma2).reverse
      }

      // Single sorted list to graph with a method actions list in each node
      val mergeableMethodActionsListTreeFuture = mergedListFuture.map { ma =>
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

        val tree = new MergeableTree[MergeableList[MethodAction]]
        methodActionsListToTreeStep(tree, tree.getRootNode, ma)
      }

      // Transform each node data from a method actions list to Measurement
      import collection.JavaConverters._
      val mergeableMeasurementTreeFuture: Future[MergeableTree[Measurement]] = mergeableMethodActionsListTreeFuture.map(mergeableMethodActionListTree => {
        mergeableMethodActionListTree.map[Measurement, MergeableNode[Measurement], MergeableTree[Measurement]](
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
      })

      mergeableMeasurementTreeFuture.pipeTo(sender())

    case Shutdown() =>
      log.debug("Received Shutdown message")
      routees foreach (_.ref ! PoisonPill)

    case Terminated(corpse) =>
      log.debug(s"Terminated($corpse)")
      router = router.removeRoutee(corpse)

      if (router.routees.isEmpty) {
        log.debug("All done! Good bye world")
        context.system.terminate()
      }
  }
}
