package com.dariosimonetti.dissertation.agent.test;

public class Main {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        System.out.println("Running agent-test with n = " + n);
        doTask(n);
        System.out.println("Done running agent-test with n = " + n);
    }

    private static void doTask(int n) {
        Executor1 executor = new Executor1();
        // 110 = 6 hours
        // 2 = 6.5 minutes
        for (int j = 0; j < 2; j++) {
            executor.execute(n);
        }
    }
}
