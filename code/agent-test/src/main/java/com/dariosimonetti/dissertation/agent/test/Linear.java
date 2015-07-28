package com.dariosimonetti.dissertation.agent.test;

import com.dariosimonetti.dissertation.agent.Measured;

public class Linear {
    @Measured
    public void quick(int n) {
        for(long i = 0; i < n*1562l; i++) {

        }
    }

    @Measured
    public void average(int n) {
        for(long i = 0; i < n*6250l; i++) {

        }
    }

    @Measured
    public void slow(int n) {
        for(long i = 0; i < n*25000l; i++) {

        }
    }
}
