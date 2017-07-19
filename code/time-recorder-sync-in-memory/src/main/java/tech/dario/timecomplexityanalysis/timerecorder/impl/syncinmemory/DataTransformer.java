package tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory;

import org.apache.commons.lang3.tuple.Pair;
import tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory.model.MethodAction;
import tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory.model.MethodFinished;
import tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory.model.MethodStarted;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;

import java.util.Iterator;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleTree;

public class DataTransformer {
  public static MergeableTree<Measurement> transform(final Iterator<MethodAction> methodActionIterator) {
    final SimpleTree<Pair<Measurement, Long>> methodActionListTree = iteratorToMeasurementsAndPartialsTree(methodActionIterator);
    return measurementsAndPartialsTreeToMeasurementTree(methodActionListTree);
  }

  private static SimpleTree<Pair<Measurement, Long>> iteratorToMeasurementsAndPartialsTree(final Iterator<MethodAction> iterator) {
    SimpleTree<Pair<Measurement, Long>> tree = new SimpleTree<>();
    SimpleNode<Pair<Measurement, Long>> currentNode = tree.getRootNode();

    while (iterator.hasNext()) {
      MethodAction ma = iterator.next();
      if (ma instanceof MethodStarted) {
        if (currentNode.hasChild(ma.getMethodLongName())) {
          SimpleNode<Pair<Measurement, Long>> childNode = currentNode.getChild(ma.getMethodLongName());

          if (childNode.getData().getRight() != null) {
            final String message = String.format("Received %s but there already was %s in this node", ma, childNode.getData().getRight());
            throw new IllegalStateException(message);
          }

          Pair<Measurement, Long> newData = Pair.of(childNode.getData().getLeft(), ma.getNanoTime());
          childNode.setData(newData);
          currentNode = childNode;
        } else {
          Pair<Measurement, Long> newData = Pair.of(Measurement.empty(), ma.getNanoTime());
          SimpleNode<Pair<Measurement, Long>> newNode = new SimpleNode<>(ma.getMethodLongName(), newData);
          currentNode = currentNode.add(newNode);
        }
      } else if (ma instanceof MethodFinished) {
        if (!currentNode.getName().equals(ma.getMethodLongName())) {
          final String message = String.format("Received %s but was expecting a MethodFinished action with name '%s'", ma, currentNode.getName());
          throw new IllegalStateException(message);
        }

        if (currentNode.getData().getRight() == null) {
          final String message = String.format("Received %s but the corresponding MethodStarted does not exist", ma);
          throw new IllegalStateException(message);
        }

        long elapsedTime = ma.getNanoTime() - currentNode.getData().getRight();
        Measurement newMeasurement = Measurement.fromElapsedTime(elapsedTime);
        Measurement mergedMeasurement = currentNode.getData().getLeft().mergeWith(newMeasurement);
        Pair<Measurement, Long> newData = Pair.of(mergedMeasurement, null);
        currentNode.setData(newData);
        currentNode = currentNode.getParent();
      } else {
        final String message = String.format("%s is neither a MethodStarted or a MethodFinished", ma);
        throw new IllegalStateException(message);
      }
    }

    if (!currentNode.getName().equals("root")) {
      final String message = String.format("Finished processing list but current node is '%s' instead of 'root'. This means some actions are missing from the list.", currentNode.getName());
      throw new IllegalStateException(message);
    }

    return tree;
  }

  private static MergeableTree<Measurement> measurementsAndPartialsTreeToMeasurementTree(SimpleTree<Pair<Measurement, Long>> tree) {
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
}
