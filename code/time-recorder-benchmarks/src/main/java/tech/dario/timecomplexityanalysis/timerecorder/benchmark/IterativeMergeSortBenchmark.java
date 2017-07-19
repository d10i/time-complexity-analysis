package tech.dario.timecomplexityanalysis.timerecorder.benchmark;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import tech.dario.timecomplexityanalysis.testalgorithms.sorting.IterativeMergeSort;

@Warmup(iterations = 5)
@Measurement(iterations = 25)
@Fork(1)
@State(Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IterativeMergeSortBenchmark {

  @Param({"1", "4", "16", "64", "256", "1024", "4096", "16384", "65536", "262144", "1048576", "4194304", "16777216", "67108864", "268435456", "1073741824"})
  public int n;

  private IterativeMergeSort iterativeMergeSort;
  private int[] array;

  @Setup(Level.Iteration)
  public void setup() throws Exception {
    iterativeMergeSort = new IterativeMergeSort();
    array = getRandomArray(n);
  }



  @Benchmark
  public void measure(final Blackhole blackhole) {
    blackhole.consume(iterativeMergeSort.sort(array));
  }

  private int[] getRandomArray(int size) {
    final Random r = new Random();
    int[] array = new int[size];
    for (int i = 0; i < size; i++) {
      array[i] = r.nextInt();
    }

    return array;
  }
}
