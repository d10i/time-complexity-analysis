package tech.dario.dissertation.timereporter.api;

public interface TimeReporterFactory {
  TimeReporter getTimeReporter(String name);
}
