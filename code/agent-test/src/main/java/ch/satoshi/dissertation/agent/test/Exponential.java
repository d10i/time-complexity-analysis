package ch.satoshi.dissertation.agent.test;

import ch.satoshi.dissertation.agent.Measured;

public class Exponential {
    @Measured
    public void quick(int n) {
        for(long i = 0; i < Math.pow(2,n)*1562l; i++) {

        }
    }

    @Measured
    public void average(int n) {
        for(long i = 0; i < Math.pow(2,n)*6250l; i++) {

        }
    }

    @Measured
    public void slow(int n) {
        for(long i = 0; i < Math.pow(2,n)*25000l; i++) {

        }
    }
}
