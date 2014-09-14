package ch.satoshi.dissertation.agent.test;

import ch.satoshi.dissertation.agent.Measured;

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
