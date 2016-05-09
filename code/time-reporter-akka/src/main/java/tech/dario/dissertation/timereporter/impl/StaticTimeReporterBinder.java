package tech.dario.dissertation.timereporter.impl;

import tech.dario.dissertation.timereporter.api.TimeReporterFactory;

public class StaticTimeReporterBinder {

  private static final StaticTimeReporterBinder SINGLETON = new StaticTimeReporterBinder();
  private static final String timeReporterFactoryClassStr = AkkaTimeReporterFactory.class.getName();

  public static final StaticTimeReporterBinder getSingleton() {
    return SINGLETON;
  }

  private final TimeReporterFactory timeReporterFactory;

  private StaticTimeReporterBinder() {
    timeReporterFactory = new AkkaTimeReporterFactory();
  }

  public TimeReporterFactory getTimeReporterFactory() {
    return timeReporterFactory;
  }

  public String getTimeReporterFactoryClassStr() {
    return timeReporterFactoryClassStr;
  }
}
