package ch.satoshi.dissertation.agent.test;

import ch.satoshi.dissertation.agent.Measured;
import ch.satoshi.dissertation.agent.MetricReporter;
import ch.satoshi.dissertation.agent.tree.Node;
import ch.satoshi.dissertation.agent.tree.RootNode;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // int n = Integer.parseInt(args[0]);

        long start;
        for (int i = 1; i <= 2; i++) {
            int n = Math.round(i * i * 1.2f);
            start = System.nanoTime();
            doTask(n);
            System.out.println(n + "\t" + String.format("%.4f", (System.nanoTime() - start) / 3000000000.0f));
            System.out.println(MetricReporter.reportAsString());
            MetricReporter.serializeToFile(new File(n + ".json"));
            MetricReporter.clear();
        }

        try {
            MetricReporter.deserializeFromFile(new File("1.json"));
            System.out.println(MetricReporter.reportAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Measured
    private static void doTask(int n) {
        Executor1 executor = new Executor1();
        for (int j = 0; j < 3; j++) {
            executor.execute(n);
        }
    }
}
