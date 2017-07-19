package tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory.model;

public class MethodFinished implements MethodAction {
  private final String methodLongName;
  private final long nanoTime;

  public MethodFinished(String methodLongName, long nanoTime) {
    this.methodLongName = methodLongName;
    this.nanoTime = nanoTime;
  }

  @Override
  public String getMethodLongName() {
    return methodLongName;
  }

  @Override
  public long getNanoTime() {
    return nanoTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MethodFinished that = (MethodFinished) o;

    if (nanoTime != that.nanoTime) return false;
    return methodLongName != null ? methodLongName.equals(that.methodLongName) : that.methodLongName == null;
  }

  @Override
  public int hashCode() {
    int result = methodLongName != null ? methodLongName.hashCode() : 0;
    result = 31 * result + (int) (nanoTime ^ (nanoTime >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "MethodFinished{" +
        "methodLongName='" + methodLongName + '\'' +
        ", nanoTime=" + nanoTime +
        '}';
  }
}
