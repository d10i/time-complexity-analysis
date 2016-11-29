package tech.dario.dissertation.agent.instrumentation;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.agent.annotations.Measured;

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
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public byte[] transform(ClassLoader loader, String fullyQualifiedClassName, Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain, byte[] classBytes) throws IllegalClassFormatException {
    if(fullyQualifiedClassName == null) {
      return null;
    }

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

      if (className.startsWith("java.") || className.startsWith("javassist.") || className.startsWith("sun.")) {
        LOGGER.debug("Skip class {}: 'java.*', 'javaassist.*' or 'sun.*'", className);
        return null;
      }

      declareAndInstantiateTimeRecorder(className, ctClass);

      boolean isClassModified = instrumentMeasuredMethods(ctClass);

      if (!isClassModified) {
        return null;
      }

      LOGGER.info("Instrumented class {}", className);

      return ctClass.toBytecode();
    } catch (Exception e) {
      LOGGER.debug("Skip class {}: {}", className, e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  private void declareAndInstantiateTimeRecorder(String className, CtClass ctClass) throws CannotCompileException, NotFoundException {
    CtField timeRecorderField = new CtField(classPool.get("tech.dario.dissertation.timerecorder.api.TimeRecorder"), "_AGENT_TIME_RECORDER", ctClass);
    timeRecorderField.setModifiers(Modifier.PRIVATE);
    timeRecorderField.setModifiers(Modifier.FINAL);
    timeRecorderField.setModifiers(Modifier.STATIC);
    ctClass.addField(timeRecorderField, "tech.dario.dissertation.timerecorder.api.TimeRecorderFactoryUtil.getTimeRecorder()");
  }

  private boolean instrumentMeasuredMethods(CtClass ctClass) throws CannotCompileException, NotFoundException {
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

        method.addLocalVariable("_agent_stackTrace", classPool.get(StackTraceElement[].class.getName()));
        method.insertBefore("_agent_stackTrace = Thread.currentThread().getStackTrace();");

        method.insertAfter("_AGENT_TIME_RECORDER.reportTime(System.nanoTime() - _agent_startTime, _agent_stackTrace);");

        isClassModified = true;
      }
    }
    return isClassModified;
  }
}
