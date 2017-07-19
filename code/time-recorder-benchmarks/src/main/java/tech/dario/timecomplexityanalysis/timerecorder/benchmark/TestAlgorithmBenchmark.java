package tech.dario.timecomplexityanalysis.timerecorder.benchmark;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import tech.dario.timecomplexityanalysis.testalgorithms.custom.TestAlgorithm;

@Warmup(iterations = 5)
@Measurement(iterations = 25)
@Fork(1)
@State(Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class TestAlgorithmBenchmark {
  private TestAlgorithm testAlgorithm;

  @Setup(Level.Iteration)
  public void setup() throws Exception {
    testAlgorithm = new TestAlgorithm();
  }

  @Benchmark
  public void measure1() {
    testAlgorithm.doTask(1);
  }

  @Benchmark
  public void measure144() {
    testAlgorithm.doTask(144);
  }

  @Benchmark
  public void measure286() {
    testAlgorithm.doTask(286);
  }

  @Benchmark
  public void measure429() {
    testAlgorithm.doTask(429);
  }
}
