package tech.dario.dissertation.timecomplexityanalysis.sdk.fitting;

public interface FittingFunction {
  double f(double n);
  double getRms();
  String toString();
}
