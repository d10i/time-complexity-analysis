package com.dariosimonetti.dissertation.agent;

import com.dariosimonetti.dissertation.agent.tree.RootNode;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MetricReporter {

    public static RootNode tree = new RootNode();

    public static void reportTime(long elapsedTime, Thread thread) {
        try {
            tree.add(elapsedTime, filterStackTraceElements(thread.getStackTrace()));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String reportAsString() {
        return tree.toString();
    }

    public static void serializeToFile(File file) {
        tree.serializeToFile(file);
    }

    public static void deserializeFromFile(File file) throws IOException {
        tree = RootNode.deserializeFromFile(file);
    }

    public static void clear() {
        tree.clear();
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
