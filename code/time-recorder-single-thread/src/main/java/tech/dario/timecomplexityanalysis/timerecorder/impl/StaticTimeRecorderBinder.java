package tech.dario.timecomplexityanalysis.timerecorder.impl;

import tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorderFactory;
import tech.dario.timecomplexityanalysis.timerecorder.impl.singlethread.AkkaTimeRecorderFactory;

public class StaticTimeRecorderBinder {

  private static final StaticTimeRecorderBinder SINGLETON = new StaticTimeRecorderBinder();
  private static final String timeRecorderFactoryClassStr = AkkaTimeRecorderFactory.class.getName();

  public static final StaticTimeRecorderBinder getSingleton() {
    return SINGLETON;
  }

  private final TimeRecorderFactory timeRecorderFactory;

  private StaticTimeRecorderBinder() {
    timeRecorderFactory = new AkkaTimeRecorderFactory();
  }

  public TimeRecorderFactory getTimeRecorderFactory() {
    return timeRecorderFactory;
  }

  public String getTimeRecorderFactoryClassStr() {
    return timeRecorderFactoryClassStr;
  }
}
