package tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory.model.MethodAction;
import tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory.model.MethodFinished;
import tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory.model.MethodStarted;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;

public class SyncInMemoryTimeRecorder implements TimeRecorder {
  private static final Logger LOGGER = LoggerFactory.getLogger(SyncInMemoryTimeRecorder.class);

  private boolean started;
  private ArrayDeque<MethodAction> deque;

  @Override
  public void start() {
    LOGGER.info("Starting {}", this);
    if (started) {
      LOGGER.warn("{} had already been started", this);
      return;
    }

    deque = new ArrayDeque<>();

    forceGarbageCollection();

    LOGGER.info("Started {}", this);
    started = true;
  }

  @Override
  public void methodStarted(String methodLongName) {
    deque.add(new MethodStarted(methodLongName, System.nanoTime()));
  }

  @Override
  public void methodFinished(String methodLongName) {
    deque.add(new MethodFinished(methodLongName, System.nanoTime()));
  }

  @Override
  public MergeableTree<Measurement> stop() throws Exception {
    LOGGER.info("Stopping {}", this);
    MergeableTree<Measurement> mergeableMeasurementTree = DataTransformer.transform(deque.iterator());
    started = false;
    return mergeableMeasurementTree;
  }

  private void forceGarbageCollection() {
    Object obj = new Object();
    WeakReference ref = new WeakReference<>(obj);
    obj = null;
    while (ref.get() != null) {
      System.gc();
    }
  }

  @Override
  public String toString() {
    return "SyncInMemoryTimeRecorder{}";
  }
}
