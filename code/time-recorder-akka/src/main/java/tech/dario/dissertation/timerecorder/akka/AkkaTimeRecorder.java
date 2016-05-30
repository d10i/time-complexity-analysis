package tech.dario.dissertation.timerecorder.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.timerecorder.api.TimeRecorder;

public class AkkaTimeRecorder implements TimeRecorder {

  private static final Logger LOGGER = LoggerFactory.getLogger(AkkaTimeRecorder.class);

  private ActorSystem actorSystem;
  private ActorRef service;
  //private ActorRef reaper;

  @Override
  public void start() {
    LOGGER.info("Starting {}", this);

    // TODO: make reference.conf work
    Config config = ConfigFactory.parseString(
      "akka {\n" +
      "\n" +
      "  version = \"2.4.6\"\n" +
      "\n" +
      "  loggers = [\"akka.event.slf4j.Slf4jLogger\"]\n" +
      "\n" +
      "  loglevel = \"DEBUG\"\n" +
      "\n" +
      "  stdout-loglevel = \"DEBUG\"\n" +
      "\n" +
      "  log-config-on-start = off\n" +
      "\n" +
      "  actor {\n" +
      "\n" +
      "    default-mailbox {\n" +
      "      mailbox-type = \"tech.dario.dissertation.timerecorder.akka.PriorityMailbox\"\n" +
      "    }\n" +
      "\n" +
      "    default-dispatcher {\n" +
      "      # Throughput defines the number of messages that are processed in a batch\n" +
      "      # before the thread is returned to the pool. Set to 1 for as fair as possible.\n" +
      "      throughput = 10\n" +
      "    }\n" +
      "\n" +
      "    deployment {\n" +
      "      /service/router1 {\n" +
      "        router = smallest-mailbox-pool\n" +
      "        nr-of-instances = 14\n" +
      "      }\n" +
      "    }\n" +
      "\n" +
      "    debug {\n" +
      "      # enable function of Actor.loggable(), which is to log any received message\n" +
      "      # at DEBUG level, see the “Testing Actor Systems” section of the Akka\n" +
      "      # Documentation at http:#akka.io/docs\n" +
      "      receive = off\n" +
      "\n" +
      "      # enable DEBUG logging of all AutoReceiveMessages (Kill, PoisonPill et.c.)\n" +
      "      autoreceive = off\n" +
      "\n" +
      "      # enable DEBUG logging of actor lifecycle changes\n" +
      "      lifecycle = off\n" +
      "\n" +
      "      # enable DEBUG logging of all LoggingFSMs for events, transitions and timers\n" +
      "      fsm = off\n" +
      "\n" +
      "      # enable DEBUG logging of subscription changes on the eventStream\n" +
      "      event-stream = off\n" +
      "\n" +
      "      # enable DEBUG logging of unhandled messages\n" +
      "      unhandled = off\n" +
      "\n" +
      "      # enable WARN logging of misconfigured routers\n" +
      "      router-misconfiguration = off\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    ).withFallback(ConfigFactory.load());
    actorSystem = ActorSystem.create("ServiceActorSystem", config);
//    actorSystem = ActorSystem.create("ServiceActorSystem");

    //LOGGER.info("{}", actorSystem.settings());

    service = actorSystem.actorOf(Props.create(ServiceActor.class), "service");
    //reaper = actorSystem.actorOf(Props.create(Reaper.class), Reaper.name());
    LOGGER.info("Started {}", this);
  }

  @Override
  public void reportTime(long elapsedTime, StackTraceElement[] stackTrace) {
    service.tell(new TimeReport(elapsedTime, stackTrace), ActorRef.noSender());
  }

  @Override
  public void stop() {
    LOGGER.info("Stopping {}", this);

    service.tell(new Save(), ActorRef.noSender());

    LOGGER.info("Awaiting actor system termination");
    actorSystem.awaitTermination();
    LOGGER.info("Actor system terminated");
    LOGGER.info("Stopped {}", this);
  }

  @Override
  public String toString() {
    return "AkkaTimeRecorder{}";
  }
}
