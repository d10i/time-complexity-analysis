package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.model.MethodAction;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.model.MethodFinished;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.model.MethodStarted;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleTree;

final class AsyncInMemoryWriter implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncInMemoryWriter.class);

  private SimpleTree<Pair<Measurement, Long>> tree;
  private SimpleNode<Pair<Measurement, Long>> currentNode;
  private BlockingDeque<MethodAction> deque;
  private boolean stopped;
  private volatile Exception exception;
  private Thread thread;

  final void open() {
    tree = new SimpleTree<>();
    currentNode = tree.getRootNode();
    deque = new LinkedBlockingDeque<>();
    stopped = false;
    exception = null;

    thread = new Thread(this);
    thread.setUncaughtExceptionHandler((t, e) -> exception = (Exception) e);
    thread.start();
  }

  final void add(final MethodAction methodAction) {
    deque.add(methodAction);
  }

  final MergeableTree<Measurement> close() throws Exception {
    stopped = true;

    // Wait for thread to finish
    thread.join();

    if (exception != null) {
      throw exception;
    }

    return measurementsAndPartialsTreeToMeasurementTree(tree);
  }

  @Override
  public final void run() {
    while (!stopped) {
      try {
        Thread.sleep(1);
        MethodAction methodAction = deque.poll();
        if (methodAction != null) {
          addMethodAction(methodAction);
        }
      } catch (InterruptedException ie) {
        LOGGER.error("Unexpected interrupted exception polling from the deque", ie);
        System.exit(1);
      }
    }

    flushDeque();
  }

  private void addMethodAction(final MethodAction methodAction) {
    if (methodAction instanceof MethodStarted) {
      if (currentNode.hasChild(methodAction.getMethodLongName())) {
        SimpleNode<Pair<Measurement, Long>> childNode = currentNode.getChild(methodAction.getMethodLongName());

        if (childNode.getData().getRight() != null) {
          final String message = String.format("Received %s but there already was %s in this node", methodAction, childNode.getData().getRight());
          throw new IllegalStateException(message);
        }

        Pair<Measurement, Long> newData = Pair.of(childNode.getData().getLeft(), methodAction.getNanoTime());
        childNode.setData(newData);
        currentNode = childNode;
      } else {
        Pair<Measurement, Long> newData = Pair.of(Measurement.empty(), methodAction.getNanoTime());
        SimpleNode<Pair<Measurement, Long>> newNode = new SimpleNode<>(methodAction.getMethodLongName(), newData);
        currentNode = currentNode.add(newNode);
      }
    } else if (methodAction instanceof MethodFinished) {
      if (!currentNode.getName().equals(methodAction.getMethodLongName())) {
        final String message = String.format("Received %s but was expecting a MethodFinished action with name '%s'", methodAction, currentNode.getName());
        throw new IllegalStateException(message);
      }

      if (currentNode.getData().getRight() == null) {
        final String message = String.format("Received %s but the corresponding MethodStarted does not exist", methodAction);
        throw new IllegalStateException(message);
      }

      long elapsedTime = methodAction.getNanoTime() - currentNode.getData().getRight();
      Measurement newMeasurement = Measurement.fromElapsedTime(elapsedTime);
      Measurement mergedMeasurement = currentNode.getData().getLeft().mergeWith(newMeasurement);
      Pair<Measurement, Long> newData = Pair.of(mergedMeasurement, null);
      currentNode.setData(newData);
      currentNode = currentNode.getParent();
    } else {
      final String message = String.format("%s is neither a MethodStarted or a MethodFinished", methodAction);
      throw new IllegalStateException(message);
    }
  }

  private MergeableTree<Measurement> measurementsAndPartialsTreeToMeasurementTree(SimpleTree<Pair<Measurement, Long>> tree) {
    if (!currentNode.getName().equals("root")) {
      final String message = String.format("Finished processing list but current node is '%s' instead of 'root'. This means some actions are missing from the list.", currentNode.getName());
      throw new IllegalStateException(message);
    }

    return tree.map(MergeableTree::new,
        node -> {
          if (node.getData() == null) {
            return new MergeableNode<>(node.getName(), null);
          } else {
            if (node.getData().getRight() != null) {
              final String message = String.format("Node with name '%s' has a pending MethodStarted %s without the corresponding MethodFinished", node.getName(), node.getData().getRight());
              throw new IllegalStateException(message);
            }

            return new MergeableNode<>(node.getName(), node.getData().getLeft());
          }
        }
    );
  }

  private void flushDeque() {
    List<MethodAction> remainingMethodActions = new ArrayList<>();
    deque.drainTo(remainingMethodActions);
    for (MethodAction remainingMethodAction : remainingMethodActions) {
      addMethodAction(remainingMethodAction);
    }
  }
}
