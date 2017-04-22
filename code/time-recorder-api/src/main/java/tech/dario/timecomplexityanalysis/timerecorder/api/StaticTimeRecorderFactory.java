// Inspired from SLF4J: https://github.com/qos-ch/slf4j/blob/master/slf4j-api/src/main/java/org/slf4j/LoggerFactory.java
package tech.dario.timecomplexityanalysis.timerecorder.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.timerecorder.impl.StaticTimeRecorderBinder;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

public final class StaticTimeRecorderFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(StaticTimeRecorderFactory.class);

  private static volatile StaticTimeRecorderFactoryStatus INITIALIZATION_STATE = StaticTimeRecorderFactoryStatus.UNINITIALIZED;

  private static String STATIC_TIME_RECORDER_BINDER_PATH = "tech/dario/timecomplexityanalysis/timerecorder/impl/StaticTimeRecorderBinder.class";
  private static String STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME = STATIC_TIME_RECORDER_BINDER_PATH.replace("/", ".").replace(".class", "");

  private StaticTimeRecorderFactory() {
  }

  public static TimeRecorder getTimeRecorder() {
    final TimeRecorderFactory timeRecorderFactory = getTimeRecorderFactory();
    return timeRecorderFactory.getTimeRecorder();
  }

  public static void reset() {
    INITIALIZATION_STATE = StaticTimeRecorderFactoryStatus.UNINITIALIZED;
  }

  private static TimeRecorderFactory getTimeRecorderFactory() {
    if (INITIALIZATION_STATE == StaticTimeRecorderFactoryStatus.UNINITIALIZED || INITIALIZATION_STATE == StaticTimeRecorderFactoryStatus.ONGOING_INITIALIZATION) {
      synchronized (StaticTimeRecorderFactory.class) {
        if (INITIALIZATION_STATE == StaticTimeRecorderFactoryStatus.UNINITIALIZED) {
          INITIALIZATION_STATE = StaticTimeRecorderFactoryStatus.ONGOING_INITIALIZATION;
          performInitialization();
        }
      }
    }

    switch (INITIALIZATION_STATE) {
      case SUCCESSFUL_INITIALIZATION:
        return StaticTimeRecorderBinder.getSingleton().getTimeRecorderFactory();
      case FAILED_INITIALIZATION:
        throw new IllegalStateException("tech.dario.timecomplexityanalysis.timerecorder.api.StaticTimeRecorderFactory could not be successfully initialized");
      case IMPL_NOT_FOUND_INITIALIZATION:
        throw new IllegalStateException("Unable to find implementation of " + STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME);
    }

    throw new IllegalStateException("Unreachable code");
  }

  private static void performInitialization() {
    bind();
  }

  private static void bind() {
    try {
      Set<URL> staticTimeRecorderBinderPathSet = findPossibleStaticTimeRecorderBinderPathSet();
      reportMultipleBindingAmbiguity(staticTimeRecorderBinderPathSet);

      StaticTimeRecorderBinder.getSingleton();
      INITIALIZATION_STATE = StaticTimeRecorderFactoryStatus.SUCCESSFUL_INITIALIZATION;
      reportActualBinding(staticTimeRecorderBinderPathSet);
    } catch (NoClassDefFoundError ncde) {
      String msg = ncde.getMessage();
      if (messageContainsStaticTimeRecorderBinder(msg)) {
        INITIALIZATION_STATE = StaticTimeRecorderFactoryStatus.IMPL_NOT_FOUND_INITIALIZATION;
        LOGGER.error("Failed to load class \"" + STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME + "\".", ncde);
      } else {
        failedBinding(ncde);
        throw ncde;
      }
    } catch (Exception e) {
      failedBinding(e);
      throw new IllegalStateException("Unexpected initialization failure", e);
    }
  }

  private static Set<URL> findPossibleStaticTimeRecorderBinderPathSet() {
    Set<URL> staticTimeRecorderBinderPathSet = new LinkedHashSet<>();
    try {
      ClassLoader timeRecorderFactoryClassLoader = StaticTimeRecorderFactory.class.getClassLoader();
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

  private static boolean messageContainsStaticTimeRecorderBinder(String msg) {
    if (msg == null) {
      return false;
    }

    if (msg.contains(STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME)) {
      return true;
    }

    return msg.contains(STATIC_TIME_RECORDER_BINDER_FULLY_QUALIFIED_CLASS_NAME.replace(".", "/"));

  }

  private static void failedBinding(Throwable t) {
    INITIALIZATION_STATE = StaticTimeRecorderFactoryStatus.FAILED_INITIALIZATION;
    LOGGER.error("Failed to instantiate StaticTimeRecorderFactory", t);
  }
}
