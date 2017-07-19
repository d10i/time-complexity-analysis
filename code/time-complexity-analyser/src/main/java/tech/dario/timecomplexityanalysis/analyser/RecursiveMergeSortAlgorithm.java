package tech.dario.timecomplexityanalysis.analyser;

import java.util.Random;
import tech.dario.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.timecomplexityanalysis.testalgorithms.sorting.RecursiveMergeSort;

public class RecursiveMergeSortAlgorithm implements Algorithm {

  private final RecursiveMergeSort recursiveMergeSort;
  private int[] array;

  public RecursiveMergeSortAlgorithm() {
    this.recursiveMergeSort = new RecursiveMergeSort();
  }

  @Override
  public void setup(long n) {
    final Random r = new Random();
    this.array = new int[(int) n];
    for (int i = 0; i < n; i++) {
      this.array[i] = r.nextInt();
    }
  }

  @Override
  public Object run() {
    return recursiveMergeSort.sort(this.array);
  }
}
