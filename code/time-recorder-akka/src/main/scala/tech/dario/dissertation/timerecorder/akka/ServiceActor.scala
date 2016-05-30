package tech.dario.dissertation.timerecorder.akka

import akka.actor._
import akka.routing._

class ServiceActor extends Actor with ActorLogging {

  val router = context.actorOf(FromConfig.props(Props[Worker]), "router1")

  override def postStop = {
    // log.info(s"$self: Router stopped")
  }

  context.watch(router)

  override def receive = {
    case timeReport: TimeReport =>
      router ! timeReport

    case s: Save =>
      //log.info("Router Save")
      router ! Broadcast(s)
      router ! Broadcast(PoisonPill)

    case Terminated(corpse) =>
      if (corpse == router) {
        // All done
        //log.info("All done! Good bye world")
        context.system.terminate()
      }
  }
}
