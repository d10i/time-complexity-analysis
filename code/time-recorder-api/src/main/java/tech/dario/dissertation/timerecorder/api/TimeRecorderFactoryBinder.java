package tech.dario.dissertation.timerecorder.api;

public interface TimeRecorderFactoryBinder {
  TimeRecorderFactory getTimeRecorderFactory();

  String getTimeRecorderFactoryClassStr();
}
