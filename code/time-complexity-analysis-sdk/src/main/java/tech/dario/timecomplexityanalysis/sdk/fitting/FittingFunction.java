package tech.dario.timecomplexityanalysis.sdk.fitting;

public interface FittingFunction {
  double f(double n);
  double getRms();
  FittingFunctionType getFittingFunctionType();
  String toString();
}
