package tech.dario.timecomplexityanalysis.testalgorithms.sorting;

public class RecursiveMergeSort implements SortingAlgorithm {

  private int[] input;
  private int[] tempMergArr;

  @Override
  public int[] sort(int input[]) {
    this.input = input;
    this.tempMergArr = new int[input.length];
    doMergeSort(0, input.length - 1);
    return this.input;
  }

  private void doMergeSort(int lowerIndex, int higherIndex) {
    if (lowerIndex < higherIndex) {
      int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
      doMergeSort(lowerIndex, middle);
      doMergeSort(middle + 1, higherIndex);
      mergeParts(lowerIndex, middle, higherIndex);
    }
  }

  private void mergeParts(int lowerIndex, int middle, int higherIndex) {
    System.arraycopy(input, lowerIndex, tempMergArr, lowerIndex, higherIndex + 1 - lowerIndex);

    int i = lowerIndex;
    int j = middle + 1;
    int k = lowerIndex;

    while (i <= middle && j <= higherIndex) {
      if (tempMergArr[i] <= tempMergArr[j]) {
        input[k] = tempMergArr[i];
        i++;
      } else {
        input[k] = tempMergArr[j];
        j++;
      }
      k++;
    }

    while (i <= middle) {
      input[k] = tempMergArr[i];
      k++;
      i++;
    }
  }
}
