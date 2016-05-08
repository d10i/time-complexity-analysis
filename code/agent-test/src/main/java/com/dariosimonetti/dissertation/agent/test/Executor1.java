package com.dariosimonetti.dissertation.agent.test;

import com.dariosimonetti.dissertation.agent.Measured;

public class Executor1 {
    private Executor2 executor2;
    private Executor3 executor3;

    private Quadratic quadratic;
    private Logarithmic logarithmic;
    private Linearithmic linearithmic;
    private Linear linear;
    private Exponential exponential;
    private Cubic cubic;
    private Constant constant;

    public Executor1() {
        executor2 = new Executor2();
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
    public void execute(int n) {
        for(long i = 0; i < 156250000l; i++) {

        }

        constant.slow(n);

        quadratic.average(n);

        for(int i = 0; i < n; i++) {
            executor2.execute(n);
        }

        linearithmic.average(n);

        executor3.execute(1);
    }
}
