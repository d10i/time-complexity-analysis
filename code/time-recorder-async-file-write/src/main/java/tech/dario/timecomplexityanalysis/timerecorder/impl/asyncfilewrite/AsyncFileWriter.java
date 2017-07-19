package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

class AsyncFileWriter<T extends Serializable> implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFileWriter.class);

  private final File file;
  private BlockingDeque<T> deque;
  private Thread thread;
  private boolean stopped = false;

  public AsyncFileWriter(final File file) throws IOException {
    this.file = file;
  }

  public void open() {
    deque = new LinkedBlockingDeque<>();
    thread = new Thread(this);
    thread.start();
  }

  public void add(final T element) {
    try {
      deque.put(element);
    } catch (InterruptedException e) {
      LOGGER.error("Unexpected interrupted exception while adding {}", element, e);
      System.exit(1);
    }
  }

  public void close() {
    stopped = true;
    try {
      // Wait for thread to finish
      thread.join();
    } catch (InterruptedException ie) {
      LOGGER.error("Unexpected interrupted exception waiting for the thread to finish", ie);
      System.exit(1);
    }
  }

  public void run() {
    try (FileOutputStream fos = new FileOutputStream(file); BufferedOutputStream bos = new BufferedOutputStream(fos); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      while (!stopped) {
        try {
          T element = deque.poll(100, TimeUnit.MICROSECONDS);
          if (element != null) {
            writeObject(oos, element);
          }
        } catch (InterruptedException ie) {
          LOGGER.error("Unexpected interrupted exception polling from the deque", ie);
          System.exit(1);
        }
      }

      flushDeque(oos);
    } catch (final FileNotFoundException fnfe) {
      LOGGER.error("File not found", fnfe);
      System.exit(1);
    } catch (final IOException ioe) {
      LOGGER.error("Unexpected I/O exception", ioe);
      System.exit(1);
    }
  }

  private void writeObject(final ObjectOutputStream oos, final T element) {
    try {
      oos.writeObject(element);
    } catch (final IOException ioe) {
      LOGGER.error("Unexpected exception writing {} to file", element, ioe);
      System.exit(1);
    }
  }

  private void flushDeque(final ObjectOutputStream oos) {
    List<T> remainingMethodActions = new ArrayList<>();
    deque.drainTo(remainingMethodActions);
    for (T remainingMethodAction : remainingMethodActions) {
      writeObject(oos, remainingMethodAction);
    }
  }
}
