package tech.dario.dissertation.timerecorder.impl;

import tech.dario.dissertation.timerecorder.api.TimeRecorderFactory;

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
