package com.dariosimonetti.dissertation.agent.test;

import com.dariosimonetti.dissertation.agent.Measured;

public class Constant {
    @Measured
    public void quick(int n) {
        for(long i = 0; i < 1562l; i++) {

        }
    }

    @Measured
    public void average(int n) {
        for(long i = 0; i < 6250l; i++) {

        }
    }

    @Measured
    public void slow(int n) {
        for(long i = 0; i < 25000l; i++) {

        }
    }
}
