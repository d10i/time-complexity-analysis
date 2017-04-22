package tech.dario.timecomplexityanalysis.sdk.domain.iterator;

import java.util.Iterator;

public class LinearIterator implements Iterator<Long> {
  private final int numIterations;
  private final int maxN;
  private int i;
  private final double a;
  private final double b;

  public LinearIterator(int numIterations, int maxN) {
    this.numIterations = numIterations;
    this.maxN = maxN;
    this.i = 0;
    this.a = (maxN - 1.0d) / (numIterations - 1.0d);
    this.b = 1.0d;
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

    return Math.round((a * i++) + b);
  }
}
