package tech.dario.dissertation.testalgorithm;

import tech.dario.dissertation.agent.annotations.Measured;

public class Executor2 {
  private Executor3 executor3;

  private Quadratic quadratic;
  private Logarithmic logarithmic;
  private Linearithmic linearithmic;
  private Linear linear;
  private Exponential exponential;
  private Cubic cubic;
  private Constant constant;

  public Executor2() {
    executor3 = new Executor3();

    constant = new Constant();
    cubic = new Cubic();
    exponential = new Exponential();
    linear = new Linear();
    linearithmic = new Linearithmic();
    logarithmic = new Logarithmic();
    quadratic = new Quadratic();
  }

  @Measured
  public void execute(long n) {
    linear.quick(n * 3);

    logarithmic.quick(n);

    exponential.quick(n);

    for (long i = 0; i < Math.round(n / 60.d); i++) {
      executor3.execute(n);
    }
  }
}
