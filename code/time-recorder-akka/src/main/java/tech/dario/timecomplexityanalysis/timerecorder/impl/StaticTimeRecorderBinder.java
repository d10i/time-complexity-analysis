package tech.dario.timecomplexityanalysis.timerecorder.impl;

import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.impl.akka.AkkaTimeRecorder;

public class StaticTimeRecorderBinder {

  private static final StaticTimeRecorderBinder SINGLETON = new StaticTimeRecorderBinder();
  private static final String timeRecorderClassStr = AkkaTimeRecorder.class.getName();

  public static final StaticTimeRecorderBinder getSingleton() {
    return SINGLETON;
  }

  private final TimeRecorder timeRecorder;

  private StaticTimeRecorderBinder() {
    timeRecorder = new AkkaTimeRecorder(1000);
  }

  public TimeRecorder getTimeRecorder() {
    return timeRecorder;
  }

  public String getTimeRecorderClassStr() {
    return timeRecorderClassStr;
  }
}
