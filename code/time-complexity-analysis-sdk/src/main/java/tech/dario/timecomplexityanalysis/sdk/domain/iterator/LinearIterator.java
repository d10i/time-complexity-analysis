package tech.dario.timecomplexityanalysis.sdk.domain.iterator;

import java.util.Iterator;

public class LinearIterator implements Iterator<Long> {
  private final int numIterations;
  private final int minN;
  private final int maxN;
  private int i;
  private final double a;
  private final double b;

  public LinearIterator(int numIterations, int maxN) {
    this(numIterations, 1, maxN);
  }

  public LinearIterator(int numIterations, int minN, int maxN) {
    this.numIterations = numIterations;
    this.minN = minN;
    this.maxN = maxN;
    this.i = 0;
    this.a = (maxN - minN) / (numIterations - 1.0d);
    this.b = minN;
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
