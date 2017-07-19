package tech.dario.timecomplexityanalysis.testalgorithms.sorting;

import static org.junit.Assert.assertTrue;

import java.util.Random;
import org.junit.Test;

public class IterativeMergeSortTest {

  @Test
  public void sort() throws Exception {
    assertArraySortedAndTime(0);
    assertArraySortedAndTime(1);
    assertArraySortedAndTime(10);
    assertArraySortedAndTime(100);
    assertArraySortedAndTime(1000);
    assertArraySortedAndTime(10000);
    assertArraySortedAndTime(100000);
    assertArraySortedAndTime(1000000);
    assertArraySortedAndTime(10000000);
    //assertArraySortedAndTime(100000000);
    //assertArraySortedAndTime(1000000000);
  }

  private void assertArraySortedAndTime(int size) {
    final IterativeMergeSort ims = new IterativeMergeSort();
    final int numIterations = 10;

    long totalTime = 0L;
    for (int i = 0; i < numIterations; i++) {
      int[] randomArray = getRandomArray(size);
      totalTime -= System.nanoTime();
      ims.sort(randomArray);
      totalTime += System.nanoTime();

      for (int j = 1; j < randomArray.length; j++) {
        assertTrue(randomArray[j - 1] <= randomArray[j]);
      }
    }

    System.out.println(size + ";" + ((double) totalTime / numIterations));
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
