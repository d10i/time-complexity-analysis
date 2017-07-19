package tech.dario.timecomplexityanalysis.testalgorithms.sorting;

public class RecursiveHeapsort implements SortingAlgorithm {

  private int[] input;

  @Override
  public int[] sort(int input[]) {
    this.input = input;
    int n = input.length;

    for (int i = n / 2 - 1; i >= 0; i--) {
      doHeapsort(n, i);
    }

    for (int i = n - 1; i >= 0; i--) {
      int temp = input[0];
      input[0] = input[i];
      input[i] = temp;

      doHeapsort(i, 0);
    }

    return this.input;
  }

  private void doHeapsort(int n, int i) {
    int largest = i;
    int l = 2 * i + 1;
    int r = 2 * i + 2;

    if (l < n && input[l] > input[largest]) {
      largest = l;
    }

    if (r < n && input[r] > input[largest]) {
      largest = r;
    }

    if (largest != i) {
      swap(i, largest);
      doHeapsort(n, largest);
    }
  }

  private void swap(int i, int j) {
    int temp = input[i];
    input[i] = input[j];
    input[j] = temp;
  }
}
