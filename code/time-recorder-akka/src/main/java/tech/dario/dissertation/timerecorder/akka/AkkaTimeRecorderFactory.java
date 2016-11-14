package tech.dario.dissertation.timerecorder.akka;

import tech.dario.dissertation.timerecorder.api.TimeRecorder;
import tech.dario.dissertation.timerecorder.api.TimeRecorderFactory;
import tech.dario.dissertation.timerecorder.tree.Metrics;
import tech.dario.dissertation.timerecorder.tree.Tree;

public class AkkaTimeRecorderFactory implements TimeRecorderFactory {

  private final TimeRecorder timeRecorder;

  public AkkaTimeRecorderFactory() {
    timeRecorder = new AkkaTimeRecorder();
  }

  @Override
  public void start() {
    timeRecorder.start();
  }

  @Override
  public TimeRecorder getTimeRecorder() {
    return timeRecorder;
  }

  @Override
  public Tree<Metrics> stop() throws Exception {
    return timeRecorder.stop();
  }

  @Override
  public String toString() {
    return "AkkaTimeRecorderFactory{}";
  }
}
