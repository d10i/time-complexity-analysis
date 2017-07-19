package tech.dario.timecomplexityanalysis.timerecorder.benchmark;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.AsyncInMemoryTimeRecorder;

@Warmup(iterations = 10, batchSize = 100000)
@Measurement(iterations = 100, batchSize = 100000)
@Fork(1)
@State(Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@OperationsPerInvocation(8)
public class AsyncInMemoryBenchmark {

  private AsyncInMemoryTimeRecorder asyncInMemoryTimeRecorder;

  @Setup(Level.Iteration)
  public void setup() throws Exception {
    asyncInMemoryTimeRecorder = new AsyncInMemoryTimeRecorder();
    asyncInMemoryTimeRecorder.start();
  }

  @Benchmark
  public void measure() {
    asyncInMemoryTimeRecorder.methodStarted("1");
    asyncInMemoryTimeRecorder.methodStarted("2");
    asyncInMemoryTimeRecorder.methodFinished("2");
    asyncInMemoryTimeRecorder.methodStarted("3");
    asyncInMemoryTimeRecorder.methodStarted("4");
    asyncInMemoryTimeRecorder.methodFinished("4");
    asyncInMemoryTimeRecorder.methodFinished("3");
    asyncInMemoryTimeRecorder.methodFinished("1");
  }

  @TearDown(Level.Iteration)
  public void tearDown(final Blackhole blackhole) throws Exception {
    blackhole.consume(asyncInMemoryTimeRecorder.stop());
  }
}
