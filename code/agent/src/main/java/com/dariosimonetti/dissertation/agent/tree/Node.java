package com.dariosimonetti.dissertation.agent.tree;

import com.dariosimonetti.dissertation.agent.MeasuredStackTraceElements;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"name", "value", "nodes"})
abstract public class Node<T extends MergeableValue> {

    @JsonProperty
    protected final Map<String, Node<T>> nodes = new HashMap<String, Node<T>>();
    //protected final List<Node<T>> nodes = new ArrayList<Node<T>>();

    public Node<T> add(T value, MeasuredStackTraceElements measuredStackTraceElements) {
        if (measuredStackTraceElements.size() > 0) {
            String stackTraceElementName = measuredStackTraceElements.getLastElement();
            Node node = nodes.get(stackTraceElementName);
            if (node != null) {
                return node.add(value, measuredStackTraceElements.withLastElementRemoved());
            }
            /*for (Node node : nodes) {
                if (node.getName().equals(stackTraceElementName)) {
                    return node.add(value, measuredStackTraceElements.withLastElementRemoved());
                }
            }*/

            TreeNode<T> newNode = new TreeNode<T>(stackTraceElementName);
            newNode.add(value, measuredStackTraceElements.withLastElementRemoved());
            nodes.put(stackTraceElementName, newNode);
            //nodes.add(newNode);
        }

        return null;
    }

    public void serializeToFile(File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(file, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    abstract String getName();

    public void clear() {
        for(Node node : this.nodes.values()) {
            node.clear();
        }
        this.nodes.clear();
    }
}
