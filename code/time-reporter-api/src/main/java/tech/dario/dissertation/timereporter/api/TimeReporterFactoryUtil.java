// Inspired from SLF4J: https://github.com/qos-ch/slf4j/blob/master/slf4j-api/src/main/java/org/slf4j/LoggerFactory.java
package tech.dario.dissertation.timereporter.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.timereporter.impl.StaticTimeReporterBinder;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

public final class TimeReporterFactoryUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeReporterFactoryUtil.class);

  private static final int UNINITIALIZED = 0;
  private static final int ONGOING_INITIALIZATION = 1;
  private static final int FAILED_INITIALIZATION = 2;
  private static final int SUCCESSFUL_INITIALIZATION = 3;
  private static final int IMPL_NOT_FOUND_INITIALIZATION = 4;

  private static volatile int INITIALIZATION_STATE = UNINITIALIZED;

  private static String STATIC_TIME_REPORTER_BINDER_PATH = "tech/dario/dissertation/timereporter/impl/StaticTimeReporterBinder.class";
  private static String STATIC_TIME_REPORTER_BINDER_FULLY_QUALIFIED_CLASS_NAME = STATIC_TIME_REPORTER_BINDER_PATH.replace("/", ".").replace(".class", "");

  private TimeReporterFactoryUtil() {
  }

  public static TimeReporterFactory getTimeReporterFactory() {
    if (INITIALIZATION_STATE == UNINITIALIZED || INITIALIZATION_STATE == ONGOING_INITIALIZATION) {
      synchronized (TimeReporterFactoryUtil.class) {
        if (INITIALIZATION_STATE == UNINITIALIZED) {
          INITIALIZATION_STATE = ONGOING_INITIALIZATION;
          performInitialization();
        }
      }
    }

    switch (INITIALIZATION_STATE) {
      case SUCCESSFUL_INITIALIZATION:
        return StaticTimeReporterBinder.getSingleton().getTimeReporterFactory();
      case FAILED_INITIALIZATION:
        throw new IllegalStateException("tech.dario.dissertation.timereporter.api.TimeReporterFactoryUtil could not be successfully initialized");
      case IMPL_NOT_FOUND_INITIALIZATION:
        throw new IllegalStateException("Unable to find implementation of " + STATIC_TIME_REPORTER_BINDER_FULLY_QUALIFIED_CLASS_NAME);
    }

    throw new IllegalStateException("Unreachable code");
  }

  public static TimeReporter getTimeReporter(String name) {
    TimeReporterFactory timeReporterFactory = getTimeReporterFactory();
    return timeReporterFactory.getTimeReporter(name);
  }

  public static TimeReporter getTimeReporter(Class<?> clazz) {
    return getTimeReporter(clazz.getName());
  }

  public static void reset() {
    INITIALIZATION_STATE = UNINITIALIZED;
  }

  private final static void performInitialization() {
    bind();
  }

  private final static void bind() {
    try {
      Set<URL> staticTimeReporterBinderPathSet = findPossibleStaticTimeReporterBinderPathSet();
      reportMultipleBindingAmbiguity(staticTimeReporterBinderPathSet);

      StaticTimeReporterBinder.getSingleton();
      INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
      reportActualBinding(staticTimeReporterBinderPathSet);
    } catch (NoClassDefFoundError ncde) {
      String msg = ncde.getMessage();
      if (messageContainsOrgImplStaticTimeReporterBinder(msg)) {
        INITIALIZATION_STATE = IMPL_NOT_FOUND_INITIALIZATION;
        LOGGER.error("Failed to load class \"" + STATIC_TIME_REPORTER_BINDER_FULLY_QUALIFIED_CLASS_NAME + "\".");
      } else {
        failedBinding(ncde);
        throw ncde;
      }
    } catch (Exception e) {
      failedBinding(e);
      throw new IllegalStateException("Unexpected initialization failure", e);
    }
  }

  static Set<URL> findPossibleStaticTimeReporterBinderPathSet() {
    Set<URL> staticTimeReporterBinderPathSet = new LinkedHashSet<>();
    try {
      ClassLoader timeReporterFactoryClassLoader = TimeReporterFactoryUtil.class.getClassLoader();
      Enumeration<URL> paths;
      if (timeReporterFactoryClassLoader == null) {
        paths = ClassLoader.getSystemResources(STATIC_TIME_REPORTER_BINDER_PATH);
      } else {
        paths = timeReporterFactoryClassLoader.getResources(STATIC_TIME_REPORTER_BINDER_PATH);
      }
      while (paths.hasMoreElements()) {
        URL path = paths.nextElement();
        staticTimeReporterBinderPathSet.add(path);
      }
    } catch (IOException ioe) {
      LOGGER.error("Error getting resources from path", ioe);
    }
    return staticTimeReporterBinderPathSet;
  }

  private static void reportMultipleBindingAmbiguity(Set<URL> binderPathSet) {
    if (isAmbiguousStaticTimeReporterBinderPathSet(binderPathSet)) {
      LOGGER.error("Class path contains multiple TimeReporter bindings.");
      for (URL path : binderPathSet) {
        LOGGER.error("Found binding in [" + path + "]");
      }
    }
  }

  private static boolean isAmbiguousStaticTimeReporterBinderPathSet(Set<URL> binderPathSet) {
    return binderPathSet.size() > 1;
  }

  private static void reportActualBinding(Set<URL> binderPathSet) {
    if (isAmbiguousStaticTimeReporterBinderPathSet(binderPathSet)) {
      LOGGER.error("Actual binding is of type [" + StaticTimeReporterBinder.getSingleton().getTimeReporterFactoryClassStr() + "]");
    }
  }

  private static boolean messageContainsOrgImplStaticTimeReporterBinder(String msg) {
    if (msg == null) {
      return false;
    }

    if (msg.contains(STATIC_TIME_REPORTER_BINDER_FULLY_QUALIFIED_CLASS_NAME.replace(".", "/"))) {
      return true;
    }

    if (msg.contains(STATIC_TIME_REPORTER_BINDER_FULLY_QUALIFIED_CLASS_NAME)) {
      return true;
    }

    return false;
  }

  static void failedBinding(Throwable t) {
    INITIALIZATION_STATE = FAILED_INITIALIZATION;
    LOGGER.error("Failed to instantiate TimeReporterFactory", t);
  }
}
