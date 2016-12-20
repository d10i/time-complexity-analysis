package tech.dario.dissertation.timecomplexityanalysis.sdk.domain.iterator;

import java.util.Iterator;

public class ExponentialIterator implements Iterator<Long> {
  private final int numIterations;
  private final int maxN;
  private int i;
  private final double base;

  public ExponentialIterator(int numIterations, int maxN) {
    this.numIterations = numIterations;
    this.maxN = maxN;
    this.i = 0;
    this.base = Math.pow(Math.E, Math.log(maxN) / (numIterations - 1.0d));
  }

  @Override
  public boolean hasNext() {
    return i < numIterations;
  }

  @Override
  public Long next() {
    if(!hasNext()) {
      return null;
    }

    return Math.round(Math.pow(base, i++));
  }
}
