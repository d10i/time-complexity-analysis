package tech.dario.timecomplexityanalysis.analyser;

import java.util.Random;
import tech.dario.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.timecomplexityanalysis.testalgorithms.sorting.RecursiveHeapsort;

public class RecursiveHeapsortAlgorithm implements Algorithm {

  private final RecursiveHeapsort recursiveHeapsort;
  private int[] array;

  public RecursiveHeapsortAlgorithm() {
    this.recursiveHeapsort = new RecursiveHeapsort();
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
    return recursiveHeapsort.sort(this.array);
  }
}
