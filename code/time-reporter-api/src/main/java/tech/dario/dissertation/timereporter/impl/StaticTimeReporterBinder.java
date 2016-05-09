package tech.dario.dissertation.timereporter.impl;

import tech.dario.dissertation.timereporter.api.TimeReporterFactory;

public class StaticTimeReporterBinder {

  private static final StaticTimeReporterBinder SINGLETON = new StaticTimeReporterBinder();

  public static final StaticTimeReporterBinder getSingleton() {
    return SINGLETON;
  }

  private StaticTimeReporterBinder() {
    throw new UnsupportedOperationException("This code should have never made it into time-reporter-api");
  }

  public TimeReporterFactory getTimeReporterFactory() {
    throw new UnsupportedOperationException("This code should never make it into time-reporter-api");
  }

  public String getTimeReporterFactoryClassStr() {
    throw new UnsupportedOperationException("This code should never make it into time-reporter-api");
  }
}
