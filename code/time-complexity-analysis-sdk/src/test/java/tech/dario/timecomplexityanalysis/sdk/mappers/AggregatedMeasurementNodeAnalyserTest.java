package tech.dario.timecomplexityanalysis.sdk.mappers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import tech.dario.timecomplexityanalysis.sdk.domain.AggregatedMeasurement;
import tech.dario.timecomplexityanalysis.sdk.domain.InterpolatedMeasurement;
import tech.dario.timecomplexityanalysis.sdk.domain.Probe;
import tech.dario.timecomplexityanalysis.sdk.fitting.ConstantFunctionFinder.ConstantFunction;
import tech.dario.timecomplexityanalysis.timerecorder.tree.Measurement;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableNode;
import tech.dario.timecomplexityanalysis.timerecorder.tree.MergeableTree;
import tech.dario.timecomplexityanalysis.timerecorder.tree.SimpleTree;

public class AggregatedMeasurementNodeAnalyserTest {
  @Test
  public void testConstant() throws Exception {
    final AggregatedMeasurement aggregatedMeasurement = new AggregatedMeasurement(1L, new Measurement(1.0d, 5.0d))
      .mergeWith(new AggregatedMeasurement(2L, new Measurement(1.0d, 5.0d)))
      .mergeWith(new AggregatedMeasurement(5L, new Measurement(1.0d, 5.0d)))
      .mergeWith(new AggregatedMeasurement(10L, new Measurement(1.0d, 5.0d)));

    final MergeableNode<AggregatedMeasurement> aggregatedMeasurementNode = new MergeableNode<>("root", aggregatedMeasurement);
    final MergeableTree<AggregatedMeasurement> aggregatedMeasurementTree = new MergeableTree<>(aggregatedMeasurementNode);

    Probe probe = mock(Probe.class);
    when(probe.getFittingFunctionTolerance()).thenReturn(0.02f);

    final SimpleTree<InterpolatedMeasurement> interpolatedTree = aggregatedMeasurementTree.map(SimpleTree::new, new AggregatedMeasurementNodeAnalyser<>(probe));

    assertEquals("Expected correct count function", new ConstantFunction(1.0d, 0.0d), interpolatedTree.getRootNode().getData().getCountFunction());
    assertEquals("Expected correct average function", new ConstantFunction(5.0d, 0.0d), interpolatedTree.getRootNode().getData().getAverageFunction());
  }
}
