package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite;

import org.junit.Test;

public class AsyncFileWriteTimeRecorderPerformanceTest {
  private AsyncFileWriteTimeRecorder asyncFileWriteTimeRecorder;

  @Test
  public void testPerformance() throws Exception {
    // Thread.sleep(10000);
    for (int i = 1; i < 18; i++) {
      int numMethodActions = (int) Math.pow(2.0d, i);
      long totalTimeTaken = 0;
      for (int j = 0; j < 50; j++) {
        asyncFileWriteTimeRecorder = new AsyncFileWriteTimeRecorder();
        asyncFileWriteTimeRecorder.start();
        long startTime = System.nanoTime();
        for (int k = 0; k < numMethodActions / 2; k++) {
          asyncFileWriteTimeRecorder.methodStarted("performanceTest");
        }
        for (int k = 0; k < numMethodActions / 2; k++) {
          asyncFileWriteTimeRecorder.methodFinished("performanceTest");
        }
        long endTime = System.nanoTime();
        asyncFileWriteTimeRecorder.stop();
        totalTimeTaken += endTime - startTime;
      }
      System.out.println(String.format("%d;%d;%.3f", numMethodActions, totalTimeTaken / 50, (totalTimeTaken / 50.0f) / numMethodActions));
    }
  }
}
