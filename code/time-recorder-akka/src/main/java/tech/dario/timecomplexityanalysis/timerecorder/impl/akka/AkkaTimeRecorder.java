package tech.dario.timecomplexityanalysis.timerecorder.impl.akka;

import akka.actor.*;
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

import java.util.ArrayList;

public class AkkaTimeRecorder implements TimeRecorder {

  private static final Logger LOGGER = LoggerFactory.getLogger(AkkaTimeRecorder.class);

  private ActorSystem actorSystem;
  private ActorRef service;
  private static boolean started;
  private ArrayList<MethodAction> list;

  private final int bufferSize;

  public AkkaTimeRecorder(int bufferSize) {
    this.bufferSize = bufferSize;
  }

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
                      "  version = \"2.5.2\"\n" +
                      "\n" +
                      "  loggers = []\n" +
                      "\n" +
                      "  loglevel = \"OFF\"\n" +
                      "\n" +
                      "  stdout-loglevel = \"OFF\"\n" +
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
                      "      fork-join-executor {\n"+
                      "        parallelism-min = 1\n"+
                      "        parallelism-factor = 1\n"+
                      "        parallelism-max = 1\n"+
                      "      }\n"+
                      "      throughput = 10000\n" +
                      "    }\n" +
                      "  }\n" +
                      "}\n"
      ).withFallback(ConfigFactory.load());
      actorSystem = ActorSystem.create("ServiceActorSystem", config);

      list = new ArrayList<>(bufferSize);

      service = actorSystem.actorOf(Props.create(ServiceActor.class), "service");
      LOGGER.info("Started {}", this);
      started = true;
    }
  }

  @Override
  public void methodStarted(String methodLongName) {
    list.add(new MethodStarted(methodLongName, System.nanoTime()));
    if (list.size() == bufferSize) {
      flushList();
    }
  }

  @Override
  public void methodFinished(String methodLongName) {
    list.add(new MethodFinished(methodLongName, System.nanoTime()));
    if (list.size() == bufferSize) {
      flushList();
    }
  }

  @Override
  public MergeableTree<Metrics> stop() throws Exception {
    synchronized (AkkaTimeRecorder.class) {
      LOGGER.info("Stopping {}", this);

      flushList();

      Timeout timeout = new Timeout(Duration.create(60, "seconds"));
      Future<Object> future = Patterns.ask(service, new Save(), timeout);
      MergeableTree<Metrics> tree = ((MergeableTree<Metrics>) Await.result(future, timeout.duration()));

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

  private void flushList() {
    service.tell(MethodActions$.MODULE$.apply(list), ActorRef.noSender());
    list = new ArrayList<>(bufferSize);
  }
}
