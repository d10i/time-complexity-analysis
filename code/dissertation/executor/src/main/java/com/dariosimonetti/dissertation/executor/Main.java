package com.dariosimonetti.dissertation.executor;

import com.dariosimonetti.dissertation.agent.Measured;
import com.dariosimonetti.dissertation.agent.MetricReporter;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        long start;
        String clazz = args[0];
        for (int i = 1; i <= 5; i++) {
            int n = Math.round(i * i * 1.3f);
            start = System.nanoTime();
            doTask(clazz, n);
            System.out.println(n + "\t" + String.format("%.4f", (System.nanoTime() - start) / 1000000000.0f));
            System.out.println(MetricReporter.reportAsString());
            MetricReporter.serializeToFile(new File(n + ".json"));
            MetricReporter.clear();
        }
    }

    @Measured
    private static int doTask(String clazz, int n) throws IOException, InterruptedException {
        System.out.println("doTask: class = " + clazz + ", n = " + n);
        ProcessBuilder pb = new ProcessBuilder("java", clazz, String.valueOf(n));
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
        pb.redirectError(ProcessBuilder.Redirect.PIPE);
        Process p = pb.start();
        int result = p.waitFor();
        System.out.println("Done doTask: class = " + clazz + ", n = " + n);
        System.out.println("Result: " + result);
        /*if(result != 0) {
            System.exit(result);
        }*/
        return result;
    }
}
