package tech.dario.timecomplexityanalysis.timerecorder.impl.singlethread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;

import java.util.ArrayDeque;

public class SingleThreadTimeRecorder implements TimeRecorder {

  private static final Logger LOGGER = LoggerFactory.getLogger(SingleThreadTimeRecorder.class);

  private static boolean started;
  private ArrayDeque<MethodAction> list;

  public static void init() {
    LOGGER.info("Initialising SingleThreadTimeRecorder");
  }

  @Override
  public void start() {
    synchronized (SingleThreadTimeRecorder.class) {
      LOGGER.info("Starting {}", this);
      if (started) {
        LOGGER.warn("{} had already been started", this);
        return;
      }

      list = new ArrayDeque<>();

      LOGGER.info("Started {}", this);
      started = true;
    }
  }

  @Override
  public void methodStarted(String methodLongName) {
    list.add(new MethodStarted(methodLongName, System.nanoTime()));
  }

  @Override
  public void methodFinished(String methodLongName) {
    list.add(new MethodFinished(methodLongName, System.nanoTime()));
  }

  @Override
  public MergeableTree<Measurement> stop() throws Exception {
    synchronized (SingleThreadTimeRecorder.class) {
      LOGGER.info("Stopping {}", this);

      MergeableTree<Measurement> mergeableMeasurementTree = Helper.methodActionsListToTree(list);

      started = false;

      return mergeableMeasurementTree;
    }
  }

  @Override
  public String toString() {
    return "SingleThreadTimeRecorder{}";
  }
}
