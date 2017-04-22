package tech.dario.timecomplexityanalysis.timerecorder.impl.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Metrics;

public class AkkaTimeRecorder implements TimeRecorder {

  private static final Logger LOGGER = LoggerFactory.getLogger(AkkaTimeRecorder.class);

  private ActorSystem actorSystem;
  private ActorRef service;
  private static boolean started;

  public static void init() {
    LOGGER.info("Initialising AkkaTimeRecorder");
  }

  @Override
  public void start() {
    synchronized (AkkaTimeRecorder.class) {
      LOGGER.info("Starting {}", this);
      if (started) {
        LOGGER.warn("{} had already been started", this);
        return;
      }

      // TODO: make reference.conf work
      Config config = ConfigFactory.parseString(
              "akka {\n" +
                      "\n" +
                      "  version = \"2.4.17\"\n" +
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
                      "      mailbox-type = \"tech.dario.timecomplexityanalysis.timerecorder.impl.akka.PriorityMailbox\"\n" +
                      "    }\n" +
                      "\n" +
                      "    default-dispatcher {\n" +
                      "      # Throughput defines the number of messages that are processed in a batch\n" +
                      "      # before the thread is returned to the pool. Set to 1 for as fair as possible.\n" +
                      "      throughput = 10\n" +
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

      service = actorSystem.actorOf(Props.create(ServiceActor.class), "service");
      LOGGER.info("Started {}", this);
      started = true;
    }
  }

  @Override
  public void reportTime(long elapsedTime, StackTraceElement[] stackTrace) {
    service.tell(new TimeReport(elapsedTime, stackTrace), ActorRef.noSender());
  }

  @Override
  public MergeableTree<Metrics> stop() throws Exception {
    synchronized (AkkaTimeRecorder.class) {
      LOGGER.info("Stopping {}", this);

      Timeout timeout = new Timeout(Duration.create(60, "seconds"));
      Future<Object> future = Patterns.ask(service, new Save(), timeout);
      MergeableTree<Metrics> tree = ((MergeableTree<Metrics>) Await.result(future, timeout.duration()));

      LOGGER.debug("Pre-normalisation: " + tree.toString());

      Future<Boolean> stopped = Patterns.gracefulStop(service, timeout.duration(), new Shutdown());
      LOGGER.debug("Awaiting actor system termination");
      Await.result(stopped, timeout.duration());
      LOGGER.debug("Actor system terminated");

      started = false;

      return tree;
    }
  }

  @Override
  public String toString() {
    return "AkkaTimeRecorder{}";
  }
}
