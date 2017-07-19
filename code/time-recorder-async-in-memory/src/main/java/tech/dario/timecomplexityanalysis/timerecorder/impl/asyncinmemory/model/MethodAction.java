package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.model;

public interface MethodAction {
  String getMethodLongName();
  long getNanoTime();
}
