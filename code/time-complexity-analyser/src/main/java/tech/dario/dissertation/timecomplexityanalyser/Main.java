package tech.dario.dissertation.timecomplexityanalyser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.testalgorithm.TestAlgorithm;
import tech.dario.dissertation.timecomplexityanalysis.sdk.TimeComplexityAnalysisSdk;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Algorithm;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private static final TimeComplexityAnalysisSdk TIME_COMPLEXITY_ANALYSIS_SDK = new TimeComplexityAnalysisSdk();
  private static final Algorithm ALGORITHM = new TestAlgorithmAlgorithm(new TestAlgorithm());

  public static void main(String args[]) {
    TIME_COMPLEXITY_ANALYSIS_SDK.analyseAlgorithm(ALGORITHM);
  }
}
