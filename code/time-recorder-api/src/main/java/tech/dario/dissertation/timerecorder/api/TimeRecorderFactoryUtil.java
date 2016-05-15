// Inspired from SLF4J: https://github.com/qos-ch/slf4j/blob/master/slf4j-api/src/main/java/org/slf4j/LoggerFactory.java
package tech.dario.dissertation.timerecorder.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.timerecorder.impl.StaticTimeRecorderBinder;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

public final class TimeRecorderFactoryUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeRecorderFactoryUtil.class);

  private static final int UNINITIALIZED = 0;
  private static final int ONGOING_INITIALIZATION = 1;
  private static final int FAILED_INITIALIZATION = 2;
  private static final int SUCCESSFUL_INITIALIZATION = 3;
  private static final int IMPL_NOT_FOUND_INITIALIZATION = 4;

  private static volatile int INITIALIZATION_STATE = UNINITIALIZED;

  private static String STATIC_TIME_RECORDER_BINDER_PATH = "tech/dario/dissertation/timerecorder/impl/StaticTimeRecorderBinder.class";
  private static String STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME = STATIC_TIME_RECORDER_BINDER_PATH.replace("/", ".").replace(".class", "");

  private TimeRecorderFactoryUtil() {
  }

  public static TimeRecorderFactory getTimeRecorderFactory() {
    if (INITIALIZATION_STATE == UNINITIALIZED || INITIALIZATION_STATE == ONGOING_INITIALIZATION) {
      synchronized (TimeRecorderFactoryUtil.class) {
        if (INITIALIZATION_STATE == UNINITIALIZED) {
          INITIALIZATION_STATE = ONGOING_INITIALIZATION;
          performInitialization();
        }
      }
    }

    switch (INITIALIZATION_STATE) {
      case SUCCESSFUL_INITIALIZATION:
        return StaticTimeRecorderBinder.getSingleton().getTimeRecorderFactory();
      case FAILED_INITIALIZATION:
        throw new IllegalStateException("tech.dario.dissertation.timerecorder.api.TimeRecorderFactoryUtil could not be successfully initialized");
      case IMPL_NOT_FOUND_INITIALIZATION:
        throw new IllegalStateException("Unable to find implementation of " + STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME);
    }

    throw new IllegalStateException("Unreachable code");
  }

  public static TimeRecorder getTimeRecorder(String name) {
    TimeRecorderFactory timeRecorderFactory = getTimeRecorderFactory();
    return timeRecorderFactory.getTimeRecorder(name);
  }

  public static TimeRecorder getTimeRecorder(Class<?> clazz) {
    return getTimeRecorder(clazz.getName());
  }

  public static void reset() {
    INITIALIZATION_STATE = UNINITIALIZED;
  }

  private final static void performInitialization() {
    bind();
  }

  private final static void bind() {
    try {
      Set<URL> staticTimeRecorderBinderPathSet = findPossibleStaticTimeRecorderBinderPathSet();
      reportMultipleBindingAmbiguity(staticTimeRecorderBinderPathSet);

      StaticTimeRecorderBinder.getSingleton();
      INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
      reportActualBinding(staticTimeRecorderBinderPathSet);
    } catch (NoClassDefFoundError ncde) {
      String msg = ncde.getMessage();
      if (messageContainsOrgImplStaticTimeRecorderBinder(msg)) {
        INITIALIZATION_STATE = IMPL_NOT_FOUND_INITIALIZATION;
        LOGGER.error("Failed to load class \"" + STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME + "\".");
      } else {
        failedBinding(ncde);
        throw ncde;
      }
    } catch (Exception e) {
      failedBinding(e);
      throw new IllegalStateException("Unexpected initialization failure", e);
    }
  }

  static Set<URL> findPossibleStaticTimeRecorderBinderPathSet() {
    Set<URL> staticTimeRecorderBinderPathSet = new LinkedHashSet<>();
    try {
      ClassLoader timeRecorderFactoryClassLoader = TimeRecorderFactoryUtil.class.getClassLoader();
      Enumeration<URL> paths;
      if (timeRecorderFactoryClassLoader == null) {
        paths = ClassLoader.getSystemResources(STATIC_TIME_RECORDER_BINDER_PATH);
      } else {
        paths = timeRecorderFactoryClassLoader.getResources(STATIC_TIME_RECORDER_BINDER_PATH);
      }
      while (paths.hasMoreElements()) {
        URL path = paths.nextElement();
        staticTimeRecorderBinderPathSet.add(path);
      }
    } catch (IOException ioe) {
      LOGGER.error("Error getting resources from path", ioe);
    }
    return staticTimeRecorderBinderPathSet;
  }

  private static void reportMultipleBindingAmbiguity(Set<URL> binderPathSet) {
    if (isAmbiguousStaticTimeRecorderBinderPathSet(binderPathSet)) {
      LOGGER.error("Class path contains multiple TimeRecorder bindings.");
      for (URL path : binderPathSet) {
        LOGGER.error("Found binding in [" + path + "]");
      }
    }
  }

  private static boolean isAmbiguousStaticTimeRecorderBinderPathSet(Set<URL> binderPathSet) {
    return binderPathSet.size() > 1;
  }

  private static void reportActualBinding(Set<URL> binderPathSet) {
    if (isAmbiguousStaticTimeRecorderBinderPathSet(binderPathSet)) {
      LOGGER.error("Actual binding is of type [" + StaticTimeRecorderBinder.getSingleton().getTimeRecorderFactoryClassStr() + "]");
    }
  }

  private static boolean messageContainsOrgImplStaticTimeRecorderBinder(String msg) {
    if (msg == null) {
      return false;
    }

    if (msg.contains(STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME.replace(".", "/"))) {
      return true;
    }

    if (msg.contains(STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME)) {
      return true;
    }

    return false;
  }

  static void failedBinding(Throwable t) {
    INITIALIZATION_STATE = FAILED_INITIALIZATION;
    LOGGER.error("Failed to instantiate TimeRecorderFactory", t);
  }
}
