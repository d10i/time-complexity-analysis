package tech.dario.dissertation.agent.instrumentation;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.agent.annotations.Measured;
import tech.dario.dissertation.timereporter.api.TimeReporter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MeasuredClassTransformer implements ClassFileTransformer {

  private final static Logger LOGGER = LoggerFactory.getLogger(MeasuredClassTransformer.class);

  private final ClassPool classPool;

  public MeasuredClassTransformer() {
    classPool = new ClassPool();
    classPool.appendSystemPath();
    try {
      classPool.appendPathList(System.getProperty("java.class.path"));

      // make sure that MetricReporter is loaded
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public byte[] transform(ClassLoader loader, String fullyQualifiedClassName, Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain, byte[] classBytes) throws IllegalClassFormatException {
    String className = fullyQualifiedClassName.replace("/", ".");

    classPool.appendClassPath(new ByteArrayClassPath(className, classBytes));

    try {
      CtClass ctClass = classPool.get(className);
      if (ctClass.isFrozen()) {
        LOGGER.debug("Skip class {}: is frozen", className);
        return null;
      }

      if (ctClass.isPrimitive() || ctClass.isArray() || ctClass.isAnnotation() || ctClass.isEnum() || ctClass.isInterface()) {
        LOGGER.debug("Skip class {}: not a class", className);
        return null;
      }

      if (className.startsWith("java.") || className.startsWith("javassist.")) {
        LOGGER.debug("Skip class {}: 'java.*' or 'javaassist.*'", className);
        return null;
      }

      declareAndInstantiateTimeReporter(className, ctClass);

      boolean isClassModified = instrumentMeasuredMethods(ctClass);

      if (!isClassModified) {
        return null;
      }

      return ctClass.toBytecode();
    } catch (Exception e) {
      LOGGER.debug("Skip class {}: {}", className, e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  private void declareAndInstantiateTimeReporter(String className, CtClass ctClass) throws CannotCompileException, NotFoundException {
    CtField timeReporterField = new CtField(classPool.get(TimeReporter.class.getName()), "_AGENT_TIME_REPORTER", ctClass);
    timeReporterField.setModifiers(Modifier.PRIVATE);
    timeReporterField.setModifiers(Modifier.FINAL);
    timeReporterField.setModifiers(Modifier.STATIC);
    ctClass.addField(timeReporterField, "tech.dario.dissertation.timereporter.api.TimeReporterFactoryUtil.getTimeReporter(" + className + ".class)");
  }

  private boolean instrumentMeasuredMethods(CtClass ctClass) throws CannotCompileException {
    boolean isClassModified = false;
    for (CtMethod method : ctClass.getDeclaredMethods()) {
      // if method is annotated, add the code to measure the time
      if (method.hasAnnotation(Measured.class)) {
        if (method.getMethodInfo().getCodeAttribute() == null) {
          LOGGER.debug("Skip method " + method.getLongName());
          continue;
        }
        LOGGER.debug("Instrumenting method " + method.getLongName());
        method.addLocalVariable("_agent_startTime", CtClass.longType);
        method.insertBefore("_agent_startTime = System.nanoTime();");
        method.insertAfter("_AGENT_TIME_REPORTER.reportTime(System.nanoTime() - _agent_startTime, Thread.currentThread());");
        isClassModified = true;
      }
    }
    return isClassModified;
  }
}