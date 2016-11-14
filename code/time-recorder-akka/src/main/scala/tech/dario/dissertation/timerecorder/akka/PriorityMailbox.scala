package tech.dario.dissertation.timerecorder.akka

import akka.actor.{ActorSystem, PoisonPill}
import akka.dispatch.{PriorityGenerator, UnboundedPriorityMailbox}
import com.typesafe.config.Config

class PriorityMailbox(settings: ActorSystem.Settings, config: Config) extends UnboundedPriorityMailbox(
  PriorityGenerator {
    case TimeReport(_, _) => 1

    case Save() => 2

    case Shutdown() => 3

    // PoisonPill must be the one with lowest priority
    case PoisonPill => 5

    case otherwise => 4
  })
