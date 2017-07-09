package tech.dario.timecomplexityanalysis.timerecorder.impl.singlethread;

import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorderFactory;

public class AkkaTimeRecorderFactory implements TimeRecorderFactory {

  private TimeRecorder timeRecorder;

  public AkkaTimeRecorderFactory() {
    timeRecorder = null;
    SingleThreadTimeRecorder.init();
  }

  @Override
  public TimeRecorder getTimeRecorder() {
    synchronized (AkkaTimeRecorderFactory.class) {
      if (timeRecorder == null) {
        timeRecorder = new SingleThreadTimeRecorder();
      }

      return timeRecorder;
    }
  }
}
