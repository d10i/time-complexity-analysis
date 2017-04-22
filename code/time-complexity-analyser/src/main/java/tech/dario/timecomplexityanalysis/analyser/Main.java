package tech.dario.timecomplexityanalysis.analyser;

import tech.dario.timecomplexityanalysis.sdk.TimeComplexityAnalysisSdk;

public class Main {

  private static final TimeComplexityAnalysisSdk TIME_COMPLEXITY_ANALYSIS_SDK = new TimeComplexityAnalysisSdk();

  public static void main(String args[]) {
    TIME_COMPLEXITY_ANALYSIS_SDK.analyseAlgorithm(new TestAlgorithmAlgorithm());
  }
}
