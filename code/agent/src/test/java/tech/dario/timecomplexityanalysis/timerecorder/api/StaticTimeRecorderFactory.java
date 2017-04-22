package tech.dario.timecomplexityanalysis.timerecorder.api;

import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;

public class StaticTimeRecorderFactory {
  public static TimeRecorder getTimeRecorder() {
    return new TimeRecorder();
  }
}
