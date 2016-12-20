package tech.dario.dissertation.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class LinearFunctionFinderTest {
  @Test
  public void findFittingFunction() throws Exception {
    final LinearFunctionFinder linearFunctionFinder = new LinearFunctionFinder();
    Optional<FittingFunction> fittingFunctionOptional = linearFunctionFinder.findFittingFunction(new WeightedObservedPoints() {{
      add(1.0d, f(1.0d));
      add(2.0d, f(2.0d));
      add(5.0d, f(5.0d));
      add(10.0d, f(10.0d));
    }}.toList());

    assertEquals("Expected correct point 1", f(1.0d), fittingFunctionOptional.get().f(1.0d), 0.0001d);
    assertEquals("Expected correct point 2", f(2.0d), fittingFunctionOptional.get().f(2.0d), 0.0001d);
    assertEquals("Expected correct point 3", f(5.0d), fittingFunctionOptional.get().f(5.0d), 0.0001d);
    assertEquals("Expected correct point 4", f(10.0d), fittingFunctionOptional.get().f(10.0d), 0.0001d);
    assertEquals("Expected correct point 5", f(50.0d), fittingFunctionOptional.get().f(50.0d), 0.0001d);
    assertEquals("Expected correct RMS", 0.000d, fittingFunctionOptional.get().getRms(), 0.0001d);
    assertEquals("Expected correct string representation", "9.000000 * n + 1.000000 [rms: 0.000000]", fittingFunctionOptional.get().toString());
  }

  private double f(double n) {
    return 9.0d * n + 1.0d;
  }
}
