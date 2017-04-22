package tech.dario.timecomplexityanalysis.agent;

import javassist.ClassPool;
import javassist.CtClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
public class MeasuringClassFileTransformerTest {
  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform1() throws Exception {
    final HashSet<String> whiteList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1$");
    }};

    final HashSet<String> blackList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1\\.method1\\(");
    }};

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass1", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass1");
    final CtClass stringCtClass = ClassPool.getDefault().get("java.lang.String");
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass, stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method3"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform2() throws Exception {
    final HashSet<String> whiteList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1$");
    }};

    final HashSet<String> blackList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1\\.method2\\(");
    }};

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass1", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass1");
    final CtClass stringCtClass = ClassPool.getDefault().get("java.lang.String");
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass, stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method3"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform3() throws Exception {
    final HashSet<String> whiteList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1\\.method1\\(");
    }};

    final HashSet<String> blackList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1$");
    }};

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass1", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass1");
    final CtClass stringCtClass = ClassPool.getDefault().get("java.lang.String");
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass, stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method3"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform4() throws Exception {
    final HashSet<String> whiteList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1\\.method1\\(");
    }};

    final HashSet<String> blackList = new HashSet<>();

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass1", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass1");
    final CtClass stringCtClass = ClassPool.getDefault().get("java.lang.String");
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass, stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method3"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform5() throws Exception {
    final HashSet<String> whiteList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1\\.method1\\(java\\.lang\\.String");
    }};

    final HashSet<String> blackList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1$");
    }};

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass1", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass1");
    final CtClass stringCtClass = ClassPool.getDefault().get("java.lang.String");
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass, stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method3"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform6() throws Exception {
    final HashSet<String> whiteList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1\\.method1\\(java\\.lang\\.String");
    }};

    final HashSet<String> blackList = new HashSet<>();

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass1", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass1");
    final CtClass stringCtClass = ClassPool.getDefault().get("java.lang.String");
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass, stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method3"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform7() throws Exception {
    final HashSet<String> whiteList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1\\.method1\\(java\\.lang\\.String,java\\.lang\\.String\\)");
    }};

    final HashSet<String> blackList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1$");
    }};

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass1", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass1");
    final CtClass stringCtClass = ClassPool.getDefault().get("java.lang.String");
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass, stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method3"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform8() throws Exception {
    final HashSet<String> whiteList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1\\.method1\\(java\\.lang\\.String,java\\.lang\\.String\\)");
    }};

    final HashSet<String> blackList = new HashSet<>();

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass1", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass1");
    final CtClass stringCtClass = ClassPool.getDefault().get("java.lang.String");
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass, stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method3"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform9() throws Exception {
    final HashSet<String> whiteList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass1$");
    }};

    final HashSet<String> blackList = new HashSet<>();

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass1", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass1");
    final CtClass stringCtClass = ClassPool.getDefault().get("java.lang.String");
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass, stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1", new CtClass[]{stringCtClass}));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method3"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform10() throws Exception {
    final HashSet<String> whiteList = new HashSet<>();

    final HashSet<String> blackList = new HashSet<>();

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass2", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass2");
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
  }

  @Test
  @PrepareForTest(MeasuringClassFileTransformer.class)
  public void testTransform11() throws Exception {
    final HashSet<String> whiteList = new HashSet<>();

    final HashSet<String> blackList = new HashSet<String>() {{
      add("^tech\\.dario\\.timecomplexityanalysis\\.agent\\.fixtures\\.MyClass2\\.method1\\(");
    }};

    final Config config = new Config(whiteList, blackList, true, true);
    final MeasuringClassFileTransformer measuringClassFileTransformer = new MeasuringClassFileTransformer(config);
    MeasuringClassFileTransformer measuringClassFileTransformerSpy = spy(measuringClassFileTransformer);

    assertNotNull(measuringClassFileTransformerSpy.transform(mock(ClassLoader.class), "tech/dario/timecomplexityanalysis/agent/fixtures/MyClass2", null, null, null));

    final CtClass myClassCtClass = ClassPool.getDefault().get("tech.dario.timecomplexityanalysis.agent.fixtures.MyClass2");
    verifyPrivate(measuringClassFileTransformerSpy, never()).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method1"));
    verifyPrivate(measuringClassFileTransformerSpy, times(1)).invoke("instrumentMethod", myClassCtClass, myClassCtClass.getDeclaredMethod("method2"));
  }
}
