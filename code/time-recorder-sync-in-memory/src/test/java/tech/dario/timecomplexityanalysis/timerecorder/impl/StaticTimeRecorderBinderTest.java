package tech.dario.timecomplexityanalysis.timerecorder.impl;

import org.junit.Test;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;

public class StaticTimeRecorderBinderTest {
  private static final TimeRecorder _AGENT_TIME_RECORDER = tech.dario.timecomplexityanalysis.timerecorder.api.StaticTimeRecorderFactory.getTimeRecorder();

  @Test
  public void test() throws Exception {
    _AGENT_TIME_RECORDER.start();
    _AGENT_TIME_RECORDER.methodStarted("tech.dario.timecomplexityanalysis.timerecorder.impl.StaticTimeRecorderBinderTest.test()");
    _AGENT_TIME_RECORDER.methodFinished("tech.dario.timecomplexityanalysis.timerecorder.impl.StaticTimeRecorderBinderTest.test()");
    _AGENT_TIME_RECORDER.stop();
  }
}
