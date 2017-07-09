package tech.dario.timecomplexityanalysis.timerecorder.impl.akka;

import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder;
import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorderFactory;

public class AkkaTimeRecorderFactory implements TimeRecorderFactory {

  private TimeRecorder timeRecorder;

  public AkkaTimeRecorderFactory() {
    timeRecorder = null;
    AkkaTimeRecorder.init();
  }

  @Override
  public TimeRecorder getTimeRecorder() {
    synchronized (AkkaTimeRecorderFactory.class) {
      if (timeRecorder == null) {
        timeRecorder = new AkkaTimeRecorder(1000);
      }

      return timeRecorder;
    }
  }
}
