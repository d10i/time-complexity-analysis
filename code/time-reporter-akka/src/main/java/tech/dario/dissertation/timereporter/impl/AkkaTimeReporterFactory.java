package tech.dario.dissertation.timereporter.impl;

import tech.dario.dissertation.timereporter.api.TimeReporter;
import tech.dario.dissertation.timereporter.api.TimeReporterFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AkkaTimeReporterFactory implements TimeReporterFactory {

  private ConcurrentMap<String, TimeReporter> timeReporterMap;

  public AkkaTimeReporterFactory() {
    timeReporterMap = new ConcurrentHashMap<>();
    AkkaTimeReporter.init();
  }

  @Override
  public TimeReporter getTimeReporter(String name) {
    TimeReporter simpleTimeReporter = timeReporterMap.get(name);
    if (simpleTimeReporter != null) {
      return simpleTimeReporter;
    } else {
      TimeReporter newInstance = new AkkaTimeReporter(name);
      TimeReporter oldInstance = timeReporterMap.putIfAbsent(name, newInstance);
      return oldInstance == null ? newInstance : oldInstance;
    }
  }
}
