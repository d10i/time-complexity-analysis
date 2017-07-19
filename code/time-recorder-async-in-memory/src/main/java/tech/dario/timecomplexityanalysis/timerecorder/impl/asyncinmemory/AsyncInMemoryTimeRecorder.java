package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.model.MethodFinished;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.model.MethodStarted;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;

import java.lang.ref.WeakReference;

public final class AsyncInMemoryTimeRecorder implements TimeRecorder {
  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncInMemoryTimeRecorder.class);

  private boolean started;
  private AsyncInMemoryWriter asyncWriter;

  @Override
  public final void start() throws Exception {
    LOGGER.info("Starting {}", this);
    if (started) {
      LOGGER.warn("{} had already been started", this);
      return;
    }

    asyncWriter = new AsyncInMemoryWriter();
    asyncWriter.open();

    forceGarbageCollection();

    LOGGER.info("Started {}", this);
    started = true;
  }

  @Override
  public final void methodStarted(String methodLongName) {
    asyncWriter.add(new MethodStarted(methodLongName, System.nanoTime()));
  }

  @Override
  public final void methodFinished(String methodLongName) {
    asyncWriter.add(new MethodFinished(methodLongName, System.nanoTime()));
  }

  @Override
  public final MergeableTree<Measurement> stop() throws Exception {
    LOGGER.info("Stopping {}", this);
    final MergeableTree<Measurement> tree = asyncWriter.close();
    started = false;
    return tree;
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
  public final String toString() {
    return "AsyncInMemoryTimeRecorder{}";
  }
}
