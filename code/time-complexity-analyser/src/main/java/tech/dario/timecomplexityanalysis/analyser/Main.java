package tech.dario.timecomplexityanalysis.analyser;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.sdk.TimeComplexityAnalysisSdk;
import tech.dario.timecomplexityanalysis.sdk.domain.InterpolatedTree;
import tech.dario.timecomplexityanalysis.sdk.domain.Probe;
import tech.dario.timecomplexityanalysis.sdk.domain.iterator.ExponentialIterator;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private static final TimeComplexityAnalysisSdk TIME_COMPLEXITY_ANALYSIS_SDK = new TimeComplexityAnalysisSdk();

  public static void main(String args[]) {
    InterpolatedTree interpolatedTree = TIME_COMPLEXITY_ANALYSIS_SDK
        .analyseAlgorithm(new IterativeMergeSortAlgorithm(), new Probe() {
          @Override
          public int getNumWarmUpRounds() {
            return 10/10;
          }

          @Override
          public int getNumRecordingRounds() {
            return 10;
          }

          @Override
          public int getNumMaxOutliers() {
            return 10/4;
          }

          @Override
          public Iterator<Long> buildNIterator() {
            return new ExponentialIterator(11, 2, 4096);
            //return new LinearIterator(6, 10000);
          }

          @Override
          public float getFittingFunctionTolerance() {
            return 0.00002f;
          }

          @Override
          public float getAssumeConstantThreshold() {
            return 100000.0f;
          }

          @Override
          public float getAssumeConstantMaxRmsRelativeError() {
            return 0.6f;
          }
        });

//    LOGGER.debug("1;{}", interpolatedTree.calculate(1));
//    LOGGER.debug("49;{}", interpolatedTree.calculate(49));
//    LOGGER.debug("96;{}", interpolatedTree.calculate(96));
//    LOGGER.debug("144;{}", interpolatedTree.calculate(144));
//    LOGGER.debug("191;{}", interpolatedTree.calculate(191));
//    LOGGER.debug("239;{}", interpolatedTree.calculate(239));
//    LOGGER.debug("286;{}", interpolatedTree.calculate(286));
//    LOGGER.debug("334;{}", interpolatedTree.calculate(334));
//    LOGGER.debug("381;{}", interpolatedTree.calculate(381));
//    LOGGER.debug("429;{}", interpolatedTree.calculate(429));
//    LOGGER.debug("477;{}", interpolatedTree.calculate(477));
//    LOGGER.debug("524;{}", interpolatedTree.calculate(524));
//    LOGGER.debug("572;{}", interpolatedTree.calculate(572));
//    LOGGER.debug("619;{}", interpolatedTree.calculate(619));
//    LOGGER.debug("667;{}", interpolatedTree.calculate(667));
//    LOGGER.debug("714;{}", interpolatedTree.calculate(714));
//    LOGGER.debug("762;{}", interpolatedTree.calculate(762));
//    LOGGER.debug("809;{}", interpolatedTree.calculate(809));
//    LOGGER.debug("857;{}", interpolatedTree.calculate(857));
//    LOGGER.debug("905;{}", interpolatedTree.calculate(905));
//    LOGGER.debug("952;{}", interpolatedTree.calculate(952));
//    LOGGER.debug("1000;{}", interpolatedTree.calculate(1000));

//    LOGGER.debug("10000;{}", interpolatedTree.calculate(10000));
//    LOGGER.debug("20000;{}", interpolatedTree.calculate(20000));
//    LOGGER.debug("30000;{}", interpolatedTree.calculate(30000));
//    LOGGER.debug("40000;{}", interpolatedTree.calculate(40000));
//    LOGGER.debug("50000;{}", interpolatedTree.calculate(50000));
//    LOGGER.debug("60000;{}", interpolatedTree.calculate(60000));
//    LOGGER.debug("70000;{}", interpolatedTree.calculate(70000));
//    LOGGER.debug("80000;{}", interpolatedTree.calculate(80000));
//    LOGGER.debug("90000;{}", interpolatedTree.calculate(90000));
//    LOGGER.debug("100000;{}", interpolatedTree.calculate(100000));
//    LOGGER.debug("110000;{}", interpolatedTree.calculate(110000));
//    LOGGER.debug("120000;{}", interpolatedTree.calculate(120000));
//    LOGGER.debug("130000;{}", interpolatedTree.calculate(130000));
//    LOGGER.debug("140000;{}", interpolatedTree.calculate(140000));
//    LOGGER.debug("150000;{}", interpolatedTree.calculate(150000));
//    LOGGER.debug("160000;{}", interpolatedTree.calculate(160000));
//    LOGGER.debug("170000;{}", interpolatedTree.calculate(170000));
//    LOGGER.debug("180000;{}", interpolatedTree.calculate(180000));
//    LOGGER.debug("190000;{}", interpolatedTree.calculate(190000));
//    LOGGER.debug("200000;{}", interpolatedTree.calculate(200000));

    LOGGER.debug("512;{}", interpolatedTree.calculate(512));
    LOGGER.debug("2048;{}", interpolatedTree.calculate(2048));
    LOGGER.debug("8192;{}", interpolatedTree.calculate(8192));
    LOGGER.debug("32768;{}", interpolatedTree.calculate(32768));
    LOGGER.debug("131072;{}", interpolatedTree.calculate(131072));
    LOGGER.debug("524288;{}", interpolatedTree.calculate(524288));
    LOGGER.debug("2097152;{}", interpolatedTree.calculate(2097152));
    LOGGER.debug("8388608;{}", interpolatedTree.calculate(8388608));
  }
}
