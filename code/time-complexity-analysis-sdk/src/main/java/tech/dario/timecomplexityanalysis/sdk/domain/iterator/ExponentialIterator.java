package tech.dario.timecomplexityanalysis.sdk.domain.iterator;

import java.util.Iterator;

public class ExponentialIterator implements Iterator<Long> {
  private final int numIterations;
  private int i;
  private final double base;
  private final int minN;

  public ExponentialIterator(int numIterations, int base) {
    this(numIterations, base, 1);
  }

  public ExponentialIterator(int numIterations, int base, int minN) {
    this.numIterations = numIterations;
    this.i = 0;
    this.base = base;
    this.minN = minN;
  }

  @Override
  public boolean hasNext() {
    return i < numIterations;
  }

  @Override
  public Long next() {
    if (!hasNext()) {
      return null;
    }

    return Math.round(minN * Math.pow(base, i++));
  }
}
