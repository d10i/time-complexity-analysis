package tech.dario.timecomplexityanalysis.timerecorder.tree;

import org.junit.Test;

import static org.junit.Assert.*;

public class MeasurementTest {
  @Test
  public void testMergeWith() throws Exception {
    Measurement measurement1 = Measurement.fromElapsedTime(100);
    Measurement measurement2 = new Measurement(10, 201);

    Measurement measurementMerge = measurement1.mergeWith(measurement2);
    assertEquals(11.0d, measurementMerge.getCount(), 0.0000000001d);
    assertEquals(301.0d, measurementMerge.getTotal(), 0.0000000001d);

    // Verify immutability
    assertEquals(1.0d, measurement1.getCount(), 0.0000000001d);
    assertEquals(100.0d, measurement1.getTotal(), 0.0000000001d);
    assertEquals(10.0d, measurement2.getCount(), 0.0000000001d);
    assertEquals(201.0d, measurement2.getTotal(), 0.0000000001d);
  }
}
