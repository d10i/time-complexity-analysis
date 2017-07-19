//package tech.dario.timecomplexityanalysis.analyser;
//
//import java.util.Random;
//import tech.dario.timecomplexityanalysis.sdk.domain.Algorithm;
//import tech.dario.timecomplexityanalysis.testalgorithms.sorting.IterativeHeapsort;
//
//public class IterativeHeapsortAlgorithm implements Algorithm {
//
//  private final IterativeHeapsort iterativeHeapsort;
//  private int[] array;
//
//  public IterativeHeapsortAlgorithm() {
//    this.iterativeHeapsort = new IterativeHeapsort();
//  }
//
//  @Override
//  public void setup(long n) {
//    final Random r = new Random();
//    this.array = new int[(int) n];
//    for (int i = 0; i < n; i++) {
//      this.array[i] = r.nextInt();
//    }
//  }
//
//  @Override
//  public Object run() {
//    return iterativeHeapsort.sort(this.array);
//  }
//}
