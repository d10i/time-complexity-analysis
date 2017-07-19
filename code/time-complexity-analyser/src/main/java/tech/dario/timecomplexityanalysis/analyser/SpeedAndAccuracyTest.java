package tech.dario.timecomplexityanalysis.analyser;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.sdk.TimeComplexityAnalysisSdk;
import tech.dario.timecomplexityanalysis.sdk.domain.InterpolatedTree;
import tech.dario.timecomplexityanalysis.sdk.domain.Probe;
import tech.dario.timecomplexityanalysis.sdk.domain.iterator.ExponentialIterator;
import tech.dario.timecomplexityanalysis.timerecorder.api.StaticTimeRecorderFactory;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;

public class SpeedAndAccuracyTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpeedAndAccuracyTest.class);

  private static final TimeComplexityAnalysisSdk TIME_COMPLEXITY_ANALYSIS_SDK = new TimeComplexityAnalysisSdk();

  public static void main(String args[]) {
    final IterativeMergeSortAlgorithm algorithm = new IterativeMergeSortAlgorithm();

    try {
//      int numMeasurements = 100;
//      for (long n = 524288; true; n *= 2) {
//        // Warmup
//        for (int i = 0; i < numMeasurements / 10; i++) {
//          algorithm.setup(n);
//          //forceGarbageCollection();
//          TimeRecorder timeRecorder = StaticTimeRecorderFactory.getTimeRecorder();
//          timeRecorder.start();
//
//          algorithm.run();
//
//          timeRecorder.stop();
//        }
//
//        long t = 0;
//        for (int i = 0; i < numMeasurements; i++) {
//          algorithm.setup(n);
//          // forceGarbageCollection();
//          TimeRecorder timeRecorder = StaticTimeRecorderFactory.getTimeRecorder();
//          timeRecorder.start();
//
//          long t0 = System.nanoTime();
//          algorithm.run();
//          long t1 = System.nanoTime();
//
//          timeRecorder.stop();
//          t += (t1 - t0);
//          LOGGER.debug("{};{}", n, (t1 - t0));
//        }
//
//        LOGGER.debug("TOTAL: {};{}", n, (double) t / numMeasurements);
//      }

      for (int numIterations = 3; true; numIterations++) {
        for (int numRecordingRounds = 10; numRecordingRounds <= 1000; numRecordingRounds *= 10) {
          for (int base = 2; base <= 8; base *= 2) {
            for (int minN = 1; minN <= 8192; minN *= 2) {
              long maxN = (long) (minN * Math.pow(base, numIterations - 1));
              if (maxN <= 262144 * 2 * 2) {
                int i = 0;
                LOGGER.debug("# {};{};{};{};{};{}", numRecordingRounds, base, numIterations, minN, maxN, i);

                final Probe probe = getProbe(numRecordingRounds, base, numIterations, minN);

                long t0 = System.nanoTime();
                final InterpolatedTree interpolatedTree = TIME_COMPLEXITY_ANALYSIS_SDK
                    .analyseAlgorithm(algorithm, probe);
                long t1 = System.nanoTime();

                LOGGER.debug("{};{};{};{};{};{};{};{}", numRecordingRounds, base, numIterations, minN, maxN, i, t1 - t0,
                    interpolatedTree.toString());
              }
            }
          }
        }
      }

//      int numRecordingRounds = 10;
//      int base = 8;
//      int numIterations = 4;
//      int minN = 512;
//      for (int i = 0; i < 100; i++) {
//        final Probe probe = getProbe(numRecordingRounds, base, numIterations, minN);
//
//        long t0 = System.nanoTime();
//        final InterpolatedTree interpolatedTree = TIME_COMPLEXITY_ANALYSIS_SDK.analyseAlgorithm(algorithm, probe);
//        long t1 = System.nanoTime();
//
//        LOGGER.debug("{};{};{}", i, t1 - t0, interpolatedTree.toString());
//      }

//      Probe probe = getSingleProbe(1000, 8);
//      TIME_COMPLEXITY_ANALYSIS_SDK.analyseAlgorithm(algorithm, probe);
//
//      Probe probe = getSingleProbe(1000, 262144);
//      TIME_COMPLEXITY_ANALYSIS_SDK.analyseAlgorithm(algorithm, probe);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Probe getProbe(int numRecordingRounds, int base, int numIterations, int minN) {
    return new Probe() {
      @Override
      public int getNumWarmUpRounds() {
        return numRecordingRounds / 10;
      }

      @Override
      public int getNumRecordingRounds() {
        return numRecordingRounds;
      }

      @Override
      public int getNumMaxOutliers() {
        return numRecordingRounds / 4;
      }

      @Override
      public Iterator<Long> buildNIterator() {
        return new ExponentialIterator(numIterations, base, minN);
      }

      @Override
      public float getFittingFunctionTolerance() {
        return 0.02f;
      }

      @Override
      public float getAssumeConstantThreshold() {
        return 100000.0f;
      }

      @Override
      public float getAssumeConstantMaxRmsRelativeError() {
        return 0.2f;
      }
    };
  }

  private static Probe getSingleProbe(int numRecordingRounds, int n) {
    return new Probe() {
      @Override
      public int getNumWarmUpRounds() {
        return numRecordingRounds / 10;
      }

      @Override
      public int getNumRecordingRounds() {
        return numRecordingRounds;
      }

      @Override
      public int getNumMaxOutliers() {
        return numRecordingRounds / 4;
      }

      @Override
      public Iterator<Long> buildNIterator() {
        return new ExponentialIterator(1, 2, n);
      }

      @Override
      public float getFittingFunctionTolerance() {
        return 0.02f;
      }

      @Override
      public float getAssumeConstantThreshold() {
        return 100000.0f;
      }

      @Override
      public float getAssumeConstantMaxRmsRelativeError() {
        return 0.2f;
      }
    };
  }

  private static void forceGarbageCollection() {
    Object obj = new Object();
    WeakReference ref = new WeakReference<>(obj);
    obj = null;
    while (ref.get() != null) {
      System.gc();
    }
  }
}
