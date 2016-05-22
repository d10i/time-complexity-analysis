package tech.dario.dissertation.timerecorder.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.timerecorder.api.TimeRecorder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AkkaTimeRecorder implements TimeRecorder {

  private static final Logger LOGGER = LoggerFactory.getLogger(AkkaTimeRecorder.class);

  @Override
  public void start() {
    // TODO
  }

  @Override
  public void reportTime(long elapsedTime, StackTraceElement[] stackTrace) {
    LOGGER.debug("reportTime({}, {})", elapsedTime, getStackTraceElementsStr(stackTrace));
  }

  private String getStackTraceElementsStr(StackTraceElement[] stackTrace) {
    Iterator<StackTraceElement> iter = Arrays.asList(stackTrace).iterator();
    StringBuilder sb = new StringBuilder();
    if (iter.hasNext()) {
      sb.append(getStackTraceElementName(iter.next()));
      while (iter.hasNext()) {
        sb.append(" -> ").append(getStackTraceElementName(iter.next()));
      }
    }

    return sb.toString();
  }

  @Override
  public void stop() {
    // TODO
  }

  private static String getStackTraceElementName(StackTraceElement stackTraceElement) {
    return stackTraceElement.getClassName().concat(".").concat(stackTraceElement.getMethodName());
  }

  @Override
  public String toString() {
    return "AkkaTimeRecorder{}";
  }
}
