package tech.dario.dissertation.timecomplexityanalysis.sdk.fitting;

import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ConstantFunctionFinderTest {
  @Test
  public void findFittingFunction() throws Exception {
    final ConstantFunctionFinder constantFunctionFinder = new ConstantFunctionFinder();
    Optional<FittingFunction> fittingFunctionOptional = constantFunctionFinder.findFittingFunction(new WeightedObservedPoints() {{
      add(1.0d, 5.0000d);
      add(2.0d, 5.0000d);
      add(5.0d, 5.0000d);
      add(10.0d, 5.0000d);
    }}.toList());

    assertEquals("Expected correct point 1", 5.0000d, fittingFunctionOptional.get().f(1.0d), 0.0001d);
    assertEquals("Expected correct point 2", 5.0000d, fittingFunctionOptional.get().f(2.0d), 0.0001d);
    assertEquals("Expected correct point 3", 5.0000d, fittingFunctionOptional.get().f(5.0d), 0.0001d);
    assertEquals("Expected correct point 4", 5.0000d, fittingFunctionOptional.get().f(10.0d), 0.0001d);
    assertEquals("Expected correct point 5", 5.0000d, fittingFunctionOptional.get().f(50.0d), 0.0001d);
    assertEquals("Expected correct RMS", 0.000d, fittingFunctionOptional.get().getRms(), 0.0001d);
    assertEquals("Expected correct string representation", "5.000000 [rms: 0.000000]", fittingFunctionOptional.get().toString());
  }

  private double f(double n) {
    return 5.0d;
  }
}
