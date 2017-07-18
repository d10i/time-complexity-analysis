package tech.dario.timecomplexityanalysis.timerecorder.impl;

import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;

public class StaticTimeRecorderBinder {

  private static final StaticTimeRecorderBinder SINGLETON = new StaticTimeRecorderBinder();

  public static final StaticTimeRecorderBinder getSingleton() {
    return SINGLETON;
  }

  private StaticTimeRecorderBinder() {
    throw new UnsupportedOperationException("This code should have never made it into time-recorder-api");
  }

  public TimeRecorder getTimeRecorder() {
    throw new UnsupportedOperationException("This code should never made it into time-recorder-api");
  }

  public String getTimeRecorderClassStr() {
    throw new UnsupportedOperationException("This code should never made it into time-recorder-api");
  }
}
