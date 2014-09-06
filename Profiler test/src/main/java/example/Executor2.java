package example;

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

    public void execute(int n) {
        linear.average(n * 3);

        logarithmic.average(n);

        //exponential.quick(n);

        for(int i = 0; i < n; i++) {
            executor3.execute(n);
        }
    }
}
