package tech.dario.timecomplexityanalysis.timerecorder.impl;

import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncinmemory.AsyncInMemoryTimeRecorder;

public final class StaticTimeRecorderBinder {

  private static final StaticTimeRecorderBinder SINGLETON = new StaticTimeRecorderBinder();
  private static final String timeRecorderClassStr = AsyncInMemoryTimeRecorder.class.getName();

  public static final StaticTimeRecorderBinder getSingleton() {
    return SINGLETON;
  }

  private final TimeRecorder timeRecorder;

  private StaticTimeRecorderBinder() {
    timeRecorder = new AsyncInMemoryTimeRecorder();
  }

  public final TimeRecorder getTimeRecorder() {
    return timeRecorder;
  }

  public final String getTimeRecorderClassStr() {
    return timeRecorderClassStr;
  }
}
