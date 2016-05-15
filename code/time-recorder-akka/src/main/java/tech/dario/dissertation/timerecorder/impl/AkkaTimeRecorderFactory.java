package tech.dario.dissertation.timerecorder.impl;

import tech.dario.dissertation.timerecorder.api.TimeRecorder;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AkkaTimeRecorderFactory implements TimeRecorderFactory {

  private ConcurrentMap<String, TimeRecorder> timeRecorderMap;

  public AkkaTimeRecorderFactory() {
    timeRecorderMap = new ConcurrentHashMap<>();
    AkkaTimeRecorder.init();
  }

  @Override
  public TimeRecorder getTimeRecorder(String name) {
    TimeRecorder simpleTimeRecorder = timeRecorderMap.get(name);
    if (simpleTimeRecorder != null) {
      return simpleTimeRecorder;
    } else {
      TimeRecorder newInstance = new AkkaTimeRecorder(name);
      TimeRecorder oldInstance = timeRecorderMap.putIfAbsent(name, newInstance);
      return oldInstance == null ? newInstance : oldInstance;
    }
  }
}
