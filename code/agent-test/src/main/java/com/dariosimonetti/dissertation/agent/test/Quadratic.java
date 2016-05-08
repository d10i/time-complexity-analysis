package com.dariosimonetti.dissertation.agent.test;

import com.dariosimonetti.dissertation.agent.Measured;

public class Quadratic {
    @Measured
    public void quick(int n) {
        for(long i = 0; i < Math.pow(n,2)*1562l; i++) {

        }
    }

    @Measured
    public void average(int n) {
        for(long i = 0; i < Math.pow(n,2)*625l; i++) {

        }
    }

    @Measured
    public void slow(int n) {
        for(long i = 0; i < Math.pow(n,2)*2500l; i++) {

        }
    }
}
