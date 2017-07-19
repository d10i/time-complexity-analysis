package tech.dario.timecomplexityanalysis.testalgorithms.sorting;

public class IterativeMergeSort implements SortingAlgorithm {

  private int[] input;
  private int[] tempMergArr;

  @Override
  public final int[] sort(int input[]) {
    this.input = input;
    this.tempMergArr = new int[input.length];
    doMergeSort();
    return this.input;
  }

  private void doMergeSort() {
    for (int k = 1; k < input.length; k *= 2) {
      yes(k);
    }
  }

  private void yes(int k) {
    for (int left = 0; left + k < input.length; left += k * 2) {
      int rght, rend;
      int m, i, j;

      rght = left + k;
      rend = rght + k;

      if (rend > input.length) {
        rend = input.length;
      }

      m = left;
      i = left;
      j = rght;

      while (i < rght && j < rend) {
        if (input[i] <= input[j]) {
          tempMergArr[m] = input[i];
          i++;
        } else {
          tempMergArr[m] = input[j];
          j++;
        }
        m++;
      }

      m = no1(rght, m, i);

      no1(rend, m, j);

      no2(left, rend);
    }
  }

  private int no1(int rght, int m, int i) {
    while (i < rght) {
      tempMergArr[m++] = input[i++];
    }
    return m;
  }

  private void no2(int left, int rend) {
    int m;
    for (m = left; m < rend; m++) {
      input[m] = tempMergArr[m];
    }
  }
}
