package tech.dario.timecomplexityanalysis.analyser;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Random;
import tech.dario.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.timecomplexityanalysis.testalgorithms.sorting.IterativeMergeSort;

public class IterativeMergeSortAlgorithm implements Algorithm {

  private final IterativeMergeSort iterativeMergeSort;
  private int[] array;

  public IterativeMergeSortAlgorithm() {
    this.iterativeMergeSort = new IterativeMergeSort();
  }

  @Override
  public void setup(long n) {
    final Random r = new Random(0);
    this.array = new int[(int) n];
    for (int i = 0; i < n; i++) {
      this.array[i] = r.nextInt();
    }
  }

  @Override
  public Object run() {
    return iterativeMergeSort.sort(this.array);
  }
}
