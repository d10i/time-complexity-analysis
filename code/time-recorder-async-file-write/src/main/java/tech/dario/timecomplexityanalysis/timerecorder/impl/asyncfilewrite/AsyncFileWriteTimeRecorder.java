package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.model.MethodAction;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.model.MethodFinished;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.model.MethodStarted;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;

import java.io.*;
import java.lang.ref.WeakReference;

public class AsyncFileWriteTimeRecorder implements TimeRecorder {
  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFileWriteTimeRecorder.class);

  private boolean started;
  private File file;
  private AsyncFileWriter<MethodAction> asyncFileWriter;

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting {}", this);
    if (started) {
      LOGGER.warn("{} had already been started", this);
      return;
    }

    file = File.createTempFile("method-actions-", ".ser");
    file.deleteOnExit();
    asyncFileWriter = new AsyncFileWriter<>(file);
    asyncFileWriter.open();

    forceGarbageCollection();

    LOGGER.info("Started {}", this);
    started = true;
  }

  @Override
  public void methodStarted(String methodLongName) {
    asyncFileWriter.add(new MethodStarted(methodLongName, System.nanoTime()));
  }

  @Override
  public void methodFinished(String methodLongName) {
    asyncFileWriter.add(new MethodFinished(methodLongName, System.nanoTime()));
  }

  @Override
  public MergeableTree<Measurement> stop() throws Exception {
    LOGGER.info("Stopping {}", this);
    asyncFileWriter.close();
    final MergeableTree<Measurement> tree = fileToTree(file);
    started = false;
    return tree;
  }

  private MergeableTree<Measurement> fileToTree(final File file) {
    try (
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis)
    ) {
      ObjectInputStreamIterator<MethodAction> iterator = new ObjectInputStreamIterator<>(ois);
      return DataTransformer.transform(iterator);
    } catch (IOException e) {
      LOGGER.error("Unexpected exception", e);
      System.exit(1);
    }

    return null;
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
    return "AsyncFileWriteTimeRecorder{}";
  }
}
