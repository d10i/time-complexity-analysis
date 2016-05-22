package tech.dario.dissertation.timerecorder.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TimeRecorderFactoryUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeRecorderFactoryUtil.class);

  private static TimeRecorderFactory timeRecorderFactory;
  private static Boolean initialised = false;

  private TimeRecorderFactoryUtil() {
  }

  public static TimeRecorder getTimeRecorder() {
    if (!initialised) {
      throw new IllegalStateException("Unable to retrieve time recorder factory because none have been set");
    }

    return timeRecorderFactory.getTimeRecorder();
  }

  public static void setTimeRecorderFactory(final TimeRecorderFactory timeRecorderFactory) {
    synchronized (TimeRecorderFactoryUtil.class) {
      if (initialised) {
        throw new IllegalStateException("Unable to set time recorder factory as it has been set before");
      }

      LOGGER.info("Setting time recorder factory: {}", timeRecorderFactory);
      TimeRecorderFactoryUtil.timeRecorderFactory = timeRecorderFactory;
      initialised = true;
    }
  }
}
