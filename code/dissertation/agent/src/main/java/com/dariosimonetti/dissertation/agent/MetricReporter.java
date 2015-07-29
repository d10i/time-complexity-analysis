package com.dariosimonetti.dissertation.agent;

import akka.actor.*;
import com.dariosimonetti.dissertation.tree.Metrics;
import com.dariosimonetti.dissertation.tree.Node;
import com.dariosimonetti.dissertation.tree.TreeRoot;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MetricReporter {

    public static TreeRoot<Metrics> tree = new TreeRoot<Metrics>();
    public static Map<MeasuredStackTraceElements, Node<Metrics>> nodesMap = new HashMap<MeasuredStackTraceElements, Node<Metrics>>();

    private static long totalInvocations = 0;
    private static double totalTime = 0.0d;

    private static final ActorRef service = createService();

    private static TimeReportsBufferBuilder timeReportsBufferBuilder = new TimeReportsBufferBuilder();

    private static ActorRef createService() {
        final ActorSystem system = ActorSystem.create("TypedActorDemo");
        return system.actorOf(Props.create(ServiceActor.class), "service");
    }

    public static void reportTime(long elapsedTime, Thread thread) {
        long start = System.nanoTime();
        try {
            System.out.println("Sending");
            service.tell(new TimeReport(elapsedTime, thread.getStackTrace()), ActorRef.noSender());

            /*timeReportsBufferBuilder.add(new TimeReport(elapsedTime, thread.getStackTrace()));

            if(totalInvocations % 10 == 0) {
                System.out.println("Sending");
                service.tell(timeReportsBufferBuilder.build(), ActorRef.noSender());
                timeReportsBufferBuilder.newInstance();
            }*/

            /*MeasuredStackTraceElements measuredStackTraceElements = MeasuredStackTraceElements.fromStackTrace(thread.getStackTrace());

            tree.add(Metrics.fromElapsedTime(elapsedTime), measuredStackTraceElements);*/
            /*Node<Metrics> node = nodesMap.get(measuredStackTraceElements);
            if (node == null) {
                node = tree.add(Metrics.fromElapsedTime(elapsedTime), measuredStackTraceElements);
                nodesMap.put(measuredStackTraceElements, node);
            } else {
                node.add(Metrics.fromElapsedTime(elapsedTime), MeasuredStackTraceElements.empty());
            }*/
        } /*catch (NotFoundException e) {
            e.printStackTrace();
        }*/ finally {
            totalInvocations++;
            totalTime += System.nanoTime() - start;
        }
    }

    public static String reportAsString() {
        return tree.toString();
    }

    public static void serializeToFile(File file) {
       //tree.serializeToFile(file);
       service.tell(timeReportsBufferBuilder.build(), ActorRef.noSender());
       service.tell(new Save(file), ActorRef.noSender());
    }

    public static TreeRoot deserializeFromFile(File file) throws IOException {
        tree = TreeRoot.deserializeFromFile(file);
        return tree;
    }

    public static void clear() {
        timeReportsBufferBuilder.newInstance();
        System.out.println(String.format("%d invocations for a total of %.4fms, average %.4fns", totalInvocations, totalTime / 1000000.0d, totalTime / totalInvocations));
        totalInvocations = 0;
        totalTime = 0.0d;
        tree.clear();
        nodesMap.clear();
    }
}
