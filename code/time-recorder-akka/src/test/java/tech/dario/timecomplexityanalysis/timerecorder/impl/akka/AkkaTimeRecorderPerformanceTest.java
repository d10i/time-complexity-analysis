package tech.dario.timecomplexityanalysis.timerecorder.impl.akka;

import org.junit.Test;

public class AkkaTimeRecorderPerformanceTest {
  private AkkaTimeRecorder akkaTimeRecorder;

  @Test
  public void testPerformance() throws Exception {
    for (int i = 1; i < 18; i++) {
      int numMethodActions = (int) Math.pow(2.0d, i);
      long totalTimeTaken = 0;
      for (int j = 0; j < 50; j++) {
        akkaTimeRecorder = new AkkaTimeRecorder(1000);
        akkaTimeRecorder.start();
        long startTime = System.nanoTime();
        for (int k = 0; k < numMethodActions / 2; k++) {
          akkaTimeRecorder.methodStarted("performanceTest");
        }
        for (int k = 0; k < numMethodActions / 2; k++) {
          akkaTimeRecorder.methodFinished("performanceTest");
        }
        long endTime = System.nanoTime();
        akkaTimeRecorder.stop();
        totalTimeTaken += endTime - startTime;
      }
      System.out.println(String.format("%d;%d;%.3f", numMethodActions, totalTimeTaken / 50, (totalTimeTaken / 50.0f) / numMethodActions));
    }
  }
}
