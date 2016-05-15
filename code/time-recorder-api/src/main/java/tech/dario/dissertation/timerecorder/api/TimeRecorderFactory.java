package tech.dario.dissertation.timerecorder.api;

public interface TimeRecorderFactory {
  TimeRecorder getTimeRecorder(String name);
}
