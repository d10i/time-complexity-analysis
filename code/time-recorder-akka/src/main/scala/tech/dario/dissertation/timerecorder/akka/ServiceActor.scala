package tech.dario.dissertation.timerecorder.akka

import akka.actor._
import akka.routing._
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import tech.dario.dissertation.timerecorder.tree.{MergeableTree, Metrics}

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ServiceActor extends Actor with ActorLogging {
  private final val NumRoutees = 3
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
    case timeReport: TimeReport =>
      router.route(timeReport, sender())

    case s: Save =>
      log.debug("Received Save message")
      val treeFutures = routees map (routee => (routee.ref ? s).mapTo[MergeableTree[Metrics]])

      val mergedTreeFuture = Future.reduce(treeFutures) {
        case (t1, t2) => t1.mergeWith(t2)
      }

      mergedTreeFuture.pipeTo(sender())

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
