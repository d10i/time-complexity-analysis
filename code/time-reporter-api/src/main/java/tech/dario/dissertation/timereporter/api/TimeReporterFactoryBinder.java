package tech.dario.dissertation.timereporter.api;

public interface TimeReporterFactoryBinder {
  TimeReporterFactory getTimeReporterFactory();

  String getTimeReporterFactoryClassStr();
}
