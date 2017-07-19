package tech.dario.timecomplexityanalysis.timerecorder.impl;

import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.AsyncFileWriteTimeRecorder;

public class StaticTimeRecorderBinder {

  private static final StaticTimeRecorderBinder SINGLETON = new StaticTimeRecorderBinder();
  private static final String timeRecorderClassStr = AsyncFileWriteTimeRecorder.class.getName();

  public static final StaticTimeRecorderBinder getSingleton() {
    return SINGLETON;
  }

  private final TimeRecorder timeRecorder;

  private StaticTimeRecorderBinder() {
    timeRecorder = new AsyncFileWriteTimeRecorder();
  }

  public TimeRecorder getTimeRecorder() {
    return timeRecorder;
  }

  public String getTimeRecorderClassStr() {
    return timeRecorderClassStr;
  }
}
