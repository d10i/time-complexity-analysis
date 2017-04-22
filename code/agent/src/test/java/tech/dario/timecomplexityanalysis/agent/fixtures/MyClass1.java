package tech.dario.timecomplexityanalysis.agent.fixtures;

import tech.dario.timecomplexityanalysis.annotations.Measured;

public class MyClass1 {
  private void method1() {
    System.out.println("method1()");
  }

  private void method1(String parameter1) {
    System.out.println("method1(String)");
  }

  private void method1(String parameter1, String parameter2) {
    System.out.println("method1(String,String)");
  }

  private void method2() {
    System.out.println("method2()");
  }

  @Measured
  private void method3() {
    System.out.println("method3()");
  }
}
