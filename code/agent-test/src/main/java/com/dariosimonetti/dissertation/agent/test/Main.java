package com.dariosimonetti.dissertation.agent.test;

import com.dariosimonetti.dissertation.agent.Measured;
import com.dariosimonetti.dissertation.agent.MetricReporter;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // int n = Integer.parseInt(args[0]);

        long start;
        for (int i = 1; i <= 5; i++) {
            int n = Math.round(i * i * 1.5f);
            start = System.nanoTime();
            doTask(n);
            System.out.println(n + "\t" + String.format("%.4f", (System.nanoTime() - start) / 1000000000.0f));
            //System.out.println(MetricReporter.reportAsString());
            MetricReporter.serializeToFile(new File(n + ".json"));
            MetricReporter.clear();
        }

        /*try {
            MetricReporter.deserializeFromFile(new File("1.json"));
            System.out.println(MetricReporter.reportAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Measured
    private static void doTask(int n) {
        Executor1 executor = new Executor1();
        // 110 = 6 hours
        // 2 = 6.5 minutes
        for (int j = 0; j < 2; j++) {
            executor.execute(n);
        }
    }
}
