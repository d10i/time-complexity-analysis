package tech.dario.timecomplexityanalysis.agent;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.timecomplexityanalysis.annotations.Measured;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MeasuringClassFileTransformer implements ClassFileTransformer {

  private final static Logger LOGGER = LoggerFactory.getLogger(MeasuringClassFileTransformer.class);

  private final static Set<Pattern> STANDARD_JAVA_LIBRARY = new HashSet<Pattern>() {{
    add(Pattern.compile("^java/"));
    add(Pattern.compile("^javax/"));
    add(Pattern.compile("^jdk/"));
    add(Pattern.compile("^sun/"));
    add(Pattern.compile("^com/oracle/net/"));
    add(Pattern.compile("^com/sun/"));
    add(Pattern.compile("^org/ietf/jgss/"));
    add(Pattern.compile("^org/jcp/xml/dsig/internal/"));
  }};

  private final static Set<Pattern> STANDARD_SCALA_LIBRARY = new HashSet<Pattern>() {{
    add(Pattern.compile("^scala/"));
  }};

  private final static Set<Pattern> ALWAYS_EXCLUDE = new HashSet<Pattern>() {{
    add(Pattern.compile("^javaassist/"));
    add(Pattern.compile("^tech/dario/timecomplexityanalysis/timerecorder/"));
  }};

  private final Config config;
  private final ClassPool classPool;
  private final Set<Pattern> whitelistPatterns;
  private final Set<Pattern> blacklistPatterns;

  public MeasuringClassFileTransformer(final Config config) {
    this.config = config;
    this.classPool = ClassPool.getDefault();
    this.whitelistPatterns = stringSetToPatternSet(config.getWhitelist());
    this.blacklistPatterns = stringSetToPatternSet(config.getBlacklist());
  }

  @Override
  public byte[] transform(final ClassLoader loader, final String fullyQualifiedClassName, final Class<?> classBeingRedefined,
                          final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {
    if (fullyQualifiedClassName == null) {
      return null;
    }

    classPool.appendClassPath(new LoaderClassPath(loader));

    String className = fullyQualifiedClassName.replace("/", ".");

    try {
      if (isClassExcludedByName(fullyQualifiedClassName)) {
        return null;
      }

      CtClass ctClass = classPool.get(className);
      if (isClassExcludedByImplementation(ctClass)) {
        return null;
      }

     declareAndInstantiateTimeRecorder(ctClass);

      boolean isClassModified = instrumentMeasuredMethods(ctClass);
      if (!isClassModified) {
        return null;
      }

      LOGGER.info("Instrumented class {}", ctClass.getName());

      return ctClass.toBytecode();
    } catch (Exception e) {
      final String message = String.format("Skip class %s: unexpected exception", className);
      LOGGER.debug(message, e);
      return null;
    }
  }

  private Set<Pattern> stringSetToPatternSet(Set<String> stringSet) {
    if (stringSet == null) {
      return null;
    }

    return stringSet
            .stream()
            .map(Pattern::compile)
            .collect(Collectors.toSet());
  }

  private boolean isClassExcludedByName(final String fullyQualifiedClassName) {
    if (config.isExcludeStandardJavaLibrary() && isMatchAtLeastOneInSet(fullyQualifiedClassName, STANDARD_JAVA_LIBRARY)) {
      LOGGER.debug("Skipping class {}: standard Java LIBRARY", fullyQualifiedClassName);
      return true;
    }

    if (config.isExcludeStandardScalaLibrary() && isMatchAtLeastOneInSet(fullyQualifiedClassName, STANDARD_SCALA_LIBRARY)) {
      LOGGER.debug("Skipping class {}: standard Scala LIBRARY", fullyQualifiedClassName);
      return true;
    }

    if (isMatchAtLeastOneInSet(fullyQualifiedClassName, ALWAYS_EXCLUDE)) {
      LOGGER.debug("Skipping class {}: always exclude", fullyQualifiedClassName);
      return true;
    }

    return false;
  }

  private boolean isClassExcludedByImplementation(final CtClass ctClass) {
    if (ctClass.isFrozen()) {
      LOGGER.debug("Skipping class {}: is frozen", ctClass.getName());
      return true;
    }

    if (ctClass.isAnnotation() || ctClass.isArray() || ctClass.isEnum() || ctClass.isInterface() || ctClass.isPrimitive()) {
      LOGGER.debug("Skipping class {}: not a class", ctClass.getName());
      return true;
    }

    return false;
  }

  private void declareAndInstantiateTimeRecorder(final CtClass ctClass) throws CannotCompileException, NotFoundException {
    CtField timeRecorderField = new CtField(classPool.get("tech.dario.timecomplexityanalysis.timerecorder.api.TimeRecorder"), "_AGENT_TIME_RECORDER", ctClass);
    timeRecorderField.setModifiers(Modifier.PRIVATE);
    timeRecorderField.setModifiers(Modifier.FINAL);
    timeRecorderField.setModifiers(Modifier.STATIC);
    ctClass.addField(timeRecorderField, "tech.dario.timecomplexityanalysis.timerecorder.api.StaticTimeRecorderFactory.getTimeRecorder()");
  }

  private boolean instrumentMeasuredMethods(final CtClass ctClass) throws CannotCompileException, NotFoundException {
    boolean isClassModified = false;
    for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
      if (isMethodMeasured(ctClass, ctMethod)) {
        instrumentMethod(ctClass, ctMethod);
        isClassModified = true;
      }
    }

    return isClassModified;
  }

  private void instrumentMethod(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
    ctMethod.addLocalVariable("_agent_startTime", CtClass.longType);
    ctMethod.insertBefore("_agent_startTime = System.nanoTime();");

    ctMethod.addLocalVariable("_agent_stackTrace", classPool.get(StackTraceElement[].class.getName()));
    ctMethod.insertBefore("_agent_stackTrace = Thread.currentThread().getStackTrace();");

    ctMethod.insertAfter("_AGENT_TIME_RECORDER.reportTime(System.nanoTime() - _agent_startTime, _agent_stackTrace);");

    final ConstPool constpool = ctClass.getClassFile().getConstPool();
    final AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
    final Annotation annotation = new Annotation("tech.dario.timecomplexityanalysis.annotations.Measured", constpool);
    attr.addAnnotation(annotation);
    ctMethod.getMethodInfo().addAttribute(attr);

    LOGGER.debug("Instrumented method {}", ctMethod.getLongName());
  }

  private boolean isMethodMeasured(final CtClass ctClass, final CtMethod ctMethod) {
    if (ctMethod.getMethodInfo().getCodeAttribute() == null) {
      LOGGER.debug("Skipping method {}: no implementation", ctMethod.getLongName());
      return false;
    }

    if (ctMethod.hasAnnotation(Measured.class)) {
      LOGGER.debug("Instrumenting method {}: has @Measured annotation", ctMethod.getLongName());
      return true;
    }

    final InstrumentationStatus methodInstrumentationStatus = getInstrumentationStatus(ctMethod.getLongName());
    if (methodInstrumentationStatus == InstrumentationStatus.WHITELISTED) {
      LOGGER.debug("Instrumenting method {}: white listed", ctMethod.getLongName());
      return true;
    }

    if (methodInstrumentationStatus == InstrumentationStatus.BLACKLISTED) {
      LOGGER.debug("Skipping method {}: black listed", ctMethod.getLongName());
      return false;
    }

    if (methodInstrumentationStatus == InstrumentationStatus.INDIFFERENT) {
      if (ctClass.hasAnnotation(Measured.class)) {
        LOGGER.debug("Instrumenting method {}: class has @Measured annotation", ctMethod.getLongName());
        return true;
      }

      final InstrumentationStatus classInstrumentationStatus = getInstrumentationStatus(ctClass.getName());
      if (classInstrumentationStatus == InstrumentationStatus.WHITELISTED) {
        LOGGER.debug("Instrumenting method {}: class is black listed", ctMethod.getLongName());
        return true;
      }

      if (classInstrumentationStatus == InstrumentationStatus.BLACKLISTED) {
        LOGGER.debug("Skipping method {}: class is black listed", ctMethod.getLongName());
        return false;
      }
    }

    LOGGER.debug("Skipping method {}: not white listed", ctMethod.getLongName());
    return false;
  }

  private boolean isMatchAtLeastOneInSet(final String entityName, final Set<Pattern> patternsSet) {
    return patternsSet
            .stream()
            .anyMatch(pattern -> pattern.matcher(entityName).find());
  }

  private InstrumentationStatus getInstrumentationStatus(final String entityName) {
    final int bestWhitelistMatch = findBestMatchingPatternLength(entityName, whitelistPatterns);
    final int bestBlacklistMatch = findBestMatchingPatternLength(entityName, blacklistPatterns);
    if (bestWhitelistMatch == 0 && bestBlacklistMatch == 0) {
      return InstrumentationStatus.INDIFFERENT;
    }

    if (bestBlacklistMatch > bestWhitelistMatch) {
      return InstrumentationStatus.BLACKLISTED;
    }

    return InstrumentationStatus.WHITELISTED;
  }

  private int findBestMatchingPatternLength(final String input, final Set<Pattern> patternsSet) {
    return patternsSet
      .stream()
      .map(pattern -> pattern.matcher(input))
      .filter(Matcher::find)
      .map(matcher -> matcher.group(0).length())
      .mapToInt(i -> i)
      .max()
      .orElse(0);
  }
}
