package tech.dario.dissertation.timerecorder.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.timerecorder.api.TimeRecorder;

public class AkkaTimeRecorder implements TimeRecorder {

  private static final Logger LOGGER = LoggerFactory.getLogger(AkkaTimeRecorder.class);

  private final String name;

  public AkkaTimeRecorder(String name) {
    this.name = name;
  }

  @Override
  public void reportTime(long elapsedTime, Thread thread) {
    LOGGER.debug("{}: reportTime({}, '{}')", name, elapsedTime, getStackTraceElementName(thread.getStackTrace()[thread.getStackTrace().length - 1]));
  }

  private static String getStackTraceElementName(StackTraceElement stackTraceElement) {
    return stackTraceElement.getClassName().concat(".").concat(stackTraceElement.getMethodName());
  }

  public static void init() {
    LOGGER.info("Initialising AkkaTimeRecorder");
    // TODO
  }
}
