package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite;

import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.model.MethodAction;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.model.MethodFinished;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.model.MethodStarted;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableList;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;

import java.util.Iterator;
import java.util.Optional;

public class DataTransformer {
  public static MergeableTree<Measurement> transform(final Iterator<MethodAction> methodActionIterator) {
    final MergeableTree<MergeableList<MethodAction>> methodActionListTree = iteratorToMethodActionListTree(methodActionIterator);
    return methodActionListTreeToMeasurementTree(methodActionListTree);
  }

  private static MergeableTree<MergeableList<MethodAction>> iteratorToMethodActionListTree(final Iterator<MethodAction> iterator) {
    MergeableTree<MergeableList<MethodAction>> methodActionListTree = new MergeableTree<>();
    MergeableNode<MergeableList<MethodAction>> currentNode = methodActionListTree.getRootNode();

    while (iterator.hasNext()) {
      MethodAction ma = iterator.next();
      if (ma instanceof MethodStarted) {
        MergeableList<MethodAction> newElement = MergeableList.fromElement(ma);
        if (currentNode.hasChild(ma.getMethodLongName())) {
          MergeableNode<MergeableList<MethodAction>> childNode = currentNode.getChild(ma.getMethodLongName());
          childNode.addData(newElement);
          currentNode = childNode;
        } else {
          MergeableNode<MergeableList<MethodAction>> newNode = new MergeableNode<>(ma.getMethodLongName(), newElement);
          currentNode = currentNode.add(newNode);
        }
      } else if (ma instanceof MethodFinished) {
        if (!currentNode.getName().equals(ma.getMethodLongName())) {
          final String message = String.format("Received %s but was expecting a MethodFinished action with name '%s'", ma, currentNode.getName());
          throw new IllegalArgumentException(message);
        }

        currentNode.addData(MergeableList.fromElement(ma));
        currentNode = currentNode.getParent();
      } else {
        final String message = String.format("%s is neither a MethodStarted or a MethodFinished", ma);
        throw new IllegalArgumentException(message);
      }
    }

    if (!currentNode.getName().equals("root")) {
      final String message = String.format("Finished processing list but current node is '%s' instead of 'root'. This means some actions are missing from the list.", currentNode.getName());
      throw new IllegalArgumentException(message);
    }

    return methodActionListTree;
  }

  private static MergeableTree<Measurement> methodActionListTreeToMeasurementTree(MergeableTree<MergeableList<MethodAction>> tree) {
    return tree.map(MergeableTree::new,
        node -> {
          if (node.getData() == null) {
            return new MergeableNode<>(node.getName(), null);
          } else {
            Measurement measurement = new Measurement(0.0d, 0.0d);
            Optional<Long> lastStartTime = Optional.empty();
            for (MethodAction ma : node.getData().getList()) {
              if (ma instanceof MethodStarted) {
                if (lastStartTime.isPresent()) {
                  throw new IllegalArgumentException("Found two consecutive MethodStarted actions");
                }

                lastStartTime = Optional.of(ma.getNanoTime());
              } else if (ma instanceof MethodFinished) {
                if (!lastStartTime.isPresent()) {
                  throw new IllegalArgumentException("Found two consecutive MethodFinished actions");
                }

                measurement = measurement.mergeWith(Measurement.fromElapsedTime(ma.getNanoTime() - lastStartTime.get()));
                lastStartTime = Optional.empty();
              } else {
                final String message = String.format("%s is neither a MethodStarted or a MethodFinished", ma);
                throw new IllegalArgumentException(message);
              }
            }

            if (lastStartTime.isPresent()) {
              final String message = String.format("Found a MethodStarted action without a matching MethodFinished. lastStartTime: %d", lastStartTime.get());
              throw new IllegalArgumentException(message);
            }

            return new MergeableNode<>(node.getName(), measurement);
          }
        }
    );
  }


}
