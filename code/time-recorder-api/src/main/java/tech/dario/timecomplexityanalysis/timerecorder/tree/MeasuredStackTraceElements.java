package tech.dario.timecomplexityanalysis.timerecorder.tree;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import tech.dario.timecomplexityanalysis.annotations.Measured;

import java.util.*;

public class MeasuredStackTraceElements {

  private static Map<String, Boolean> measuredStackTraceElementNamesCache = new HashMap<>();
  private static ClassPool classPool = getClassPool();

  private static ClassPool getClassPool() {
    ClassPool classPool = ClassPool.getDefault();
    classPool.appendSystemPath();
    try {
      classPool.appendPathList(System.getProperty("java.class.path"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return classPool;
  }

  private final List<String> stackTraceElementNames;
  private final int hashCode;

  private MeasuredStackTraceElements(List<String> stackTraceElementNames) {
    this.stackTraceElementNames = stackTraceElementNames;
    this.hashCode = calculateHashCode();
  }

  public static MeasuredStackTraceElements fromStackTrace(StackTraceElement[] stackTrace) throws NotFoundException {
    List<String> measuredStackTraceElementNames = new ArrayList<>(stackTrace.length);
    for (StackTraceElement stackTraceElement : stackTrace) {
      final String stackTraceElementName = getStackTraceElementName(stackTraceElement);
      Boolean measuredMethod = measuredStackTraceElementNamesCache.get(stackTraceElementName);
      if (measuredMethod == null) {
        measuredMethod = isMeasuredMethod(stackTraceElement);
        measuredStackTraceElementNamesCache.put(stackTraceElementName, measuredMethod);
      }

      if (measuredMethod) {
        measuredStackTraceElementNames.add(stackTraceElementName);
      }
    }

    return new MeasuredStackTraceElements(measuredStackTraceElementNames);
  }

  private static String getStackTraceElementName(StackTraceElement stackTraceElement) {
    return stackTraceElement.getClassName().concat(".").concat(stackTraceElement.getMethodName());
    stackTraceElement.
  }

  private static boolean isMeasuredMethod(StackTraceElement stackTraceElement) throws NotFoundException {
    CtClass ctClass = classPool.get(stackTraceElement.getClassName());
    CtMethod ctMethod = ctClass.getDeclaredMethod(stackTraceElement.getMethodName());
    return ctMethod.hasAnnotation(Measured.class);
  }

  public MeasuredStackTraceElements withLastElementRemoved() {
    return new MeasuredStackTraceElements(stackTraceElementNames.subList(0, stackTraceElementNames.size() - 1));
  }

  public String getLastElement() {
    return stackTraceElementNames.get(stackTraceElementNames.size() - 1);
  }

  public int size() {
    return stackTraceElementNames.size();
  }

  private int calculateHashCode() {
    return stackTraceElementNames != null ? stackTraceElementNames.hashCode() : 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MeasuredStackTraceElements that = (MeasuredStackTraceElements) o;

    return stackTraceElementNames != null ? stackTraceElementNames.equals(that.stackTraceElementNames) : that.stackTraceElementNames == null;
  }

  @Override
  public synchronized int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return Arrays.toString(stackTraceElementNames.toArray());
  }
}
