package com.dariosimonetti.dissertation.agent;

import com.dariosimonetti.dissertation.agent.complexityreport.ComplexityReportBuilder;
import com.dariosimonetti.dissertation.agent.tree.Metrics;
import com.dariosimonetti.dissertation.agent.tree.Node;
import com.dariosimonetti.dissertation.agent.tree.TreeRoot;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MetricReporter {

    public static TreeRoot<Metrics> tree = new TreeRoot<Metrics>();
    public static Map<MeasuredStackTraceElements, Node<Metrics>> nodesMap = new HashMap<MeasuredStackTraceElements, Node<Metrics>>();

    private static long totalInvocations = 0;
    private static double totalTime = 0.0d;

    public static void reportTime(long elapsedTime, Thread thread) {
        long start = System.nanoTime();
        try {
            MeasuredStackTraceElements measuredStackTraceElements = MeasuredStackTraceElements.fromThread(thread);

            //tree.add(Metrics.fromElapsedTime(elapsedTime), measuredStackTraceElements);
            Node<Metrics> node = nodesMap.get(measuredStackTraceElements);
            if (node == null) {
                node = tree.add(Metrics.fromElapsedTime(elapsedTime), measuredStackTraceElements);
                nodesMap.put(measuredStackTraceElements, node);
            } else {
                node.add(Metrics.fromElapsedTime(elapsedTime), MeasuredStackTraceElements.empty());
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        } finally {
            totalInvocations++;
            totalTime += System.nanoTime() - start;
        }
    }

    public static void saveComplexityReport(Map<Integer, File> inputReports, File outputReport) {
        try {
            ComplexityReportBuilder complexityReportBuilder = ComplexityReportBuilder.newInstance();
            for (Map.Entry<Integer, File> entry : inputReports.entrySet()) {
                complexityReportBuilder.add(entry.getKey(), MetricReporter.deserializeFromFile(entry.getValue()));
            }
            System.out.println(complexityReportBuilder.reportAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String reportAsString() {
        return tree.toString();
    }

    public static void serializeToFile(File file) {
        tree.serializeToFile(file);
    }

    public static TreeRoot deserializeFromFile(File file) throws IOException {
        tree = TreeRoot.deserializeFromFile(file);
        return tree;
    }

    public static void clear() {
        System.out.println(String.format("%d invocations for a total of %.4fms, average %.4fns", totalInvocations, totalTime / 1000000.0d, totalTime / totalInvocations));
        totalInvocations = 0;
        totalTime = 0.0d;
        tree.clear();
        nodesMap.clear();
    }

    private static List<StackTraceElement> filterStackTraceElements(StackTraceElement[] stackTraceElements) throws NotFoundException {
        List<StackTraceElement> stackTraceElementsList = new ArrayList<StackTraceElement>(stackTraceElements.length);
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            CtClass ctClass = ClassPool.getDefault().get(stackTraceElement.getClassName());
            CtMethod ctMethod = ctClass.getDeclaredMethod(stackTraceElement.getMethodName());
            if (ctMethod.hasAnnotation(Measured.class)) {
                stackTraceElementsList.add(stackTraceElement);
            }
        }

        return stackTraceElementsList;
    }
}
