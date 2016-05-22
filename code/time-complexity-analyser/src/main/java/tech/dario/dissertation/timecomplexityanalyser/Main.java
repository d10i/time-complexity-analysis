package tech.dario.dissertation.timecomplexityanalyser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.testalgorithm.TestAlgorithm;
import tech.dario.dissertation.timecomplexityanalysis.sdk.TimeComplexityAnalysisSdk;
import tech.dario.dissertation.timecomplexityanalysis.sdk.domain.Algorithm;
import tech.dario.dissertation.timerecorder.impl.AkkaTimeRecorderFactory;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private static final TimeComplexityAnalysisSdk TIME_COMPLEXITY_ANALYSIS_SDK = new TimeComplexityAnalysisSdk(new AkkaTimeRecorderFactory());

  public static void main(String args[]) {
    Algorithm algorithm = new TestAlgorithmAlgorithm(new TestAlgorithm());
    TIME_COMPLEXITY_ANALYSIS_SDK.analyseAlgorithm(algorithm);
  }
}
