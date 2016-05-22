package tech.dario.dissertation.timerecorder.api;

public interface TimeRecorderFactory {
  void start();

  TimeRecorder getTimeRecorder();

  void stop();
}
