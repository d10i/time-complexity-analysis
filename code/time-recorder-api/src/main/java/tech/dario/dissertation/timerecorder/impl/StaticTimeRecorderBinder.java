package tech.dario.dissertation.timerecorder.impl;

import tech.dario.dissertation.timerecorder.api.TimeRecorderFactory;

public class StaticTimeRecorderBinder {

  private static final StaticTimeRecorderBinder SINGLETON = new StaticTimeRecorderBinder();

  public static final StaticTimeRecorderBinder getSingleton() {
    return SINGLETON;
  }

  private StaticTimeRecorderBinder() {
    throw new UnsupportedOperationException("This code should have never made it into time-recorder-api");
  }

  public TimeRecorderFactory getTimeRecorderFactory() {
    throw new UnsupportedOperationException("This code should never make it into time-recorder-api");
  }

  public String getTimeRecorderFactoryClassStr() {
    throw new UnsupportedOperationException("This code should never make it into time-recorder-api");
  }
}
