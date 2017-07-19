package tech.dario.timecomplexityanalysis.timerecorder.impl.syncinmemory.model;

import java.io.Serializable;

public interface MethodAction extends Serializable {
  String getMethodLongName();
  long getNanoTime();
}
