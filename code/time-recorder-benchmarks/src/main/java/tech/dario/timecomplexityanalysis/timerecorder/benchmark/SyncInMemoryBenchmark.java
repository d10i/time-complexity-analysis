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
import tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory.SyncInMemoryTimeRecorder;

@Warmup(iterations = 10, batchSize = 100000)
@Measurement(iterations = 100, batchSize = 100000)
@Fork(1)
@State(Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@OperationsPerInvocation(8)
public class SyncInMemoryBenchmark {
  private SyncInMemoryTimeRecorder syncInMemoryTimeRecorder;

  @Setup(Level.Iteration)
  public void setup() throws Exception {
    syncInMemoryTimeRecorder = new SyncInMemoryTimeRecorder();
    syncInMemoryTimeRecorder.start();
  }

  @Benchmark
  public void measure() {
    syncInMemoryTimeRecorder.methodStarted("1");
    syncInMemoryTimeRecorder.methodStarted("2");
    syncInMemoryTimeRecorder.methodFinished("2");
    syncInMemoryTimeRecorder.methodStarted("3");
    syncInMemoryTimeRecorder.methodStarted("4");
    syncInMemoryTimeRecorder.methodFinished("4");
    syncInMemoryTimeRecorder.methodFinished("3");
    syncInMemoryTimeRecorder.methodFinished("1");
  }

  @TearDown(Level.Iteration)
  public void tearDown(final Blackhole blackhole) throws Exception {
    blackhole.consume(syncInMemoryTimeRecorder.stop());
  }
}
