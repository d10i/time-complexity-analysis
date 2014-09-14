package com.dariosimonetti.dissertation.agent.tree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "name", "count", "total", "min", "max", "nodes" })
abstract public class Node {

    @JsonProperty
    protected final List<TreeNode> nodes = new ArrayList<TreeNode>();

    public void add(long elapsedTime, List<StackTraceElement> stackTraceElementsList) {
        if (stackTraceElementsList.size() > 0) {
            StackTraceElement stackTraceElement = stackTraceElementsList.get(stackTraceElementsList.size() - 1);
            String stackTraceElementName = getStackTraceElementName(stackTraceElement);
            boolean found = false;
            for (Node node : nodes) {
                if (node.getName().equals(stackTraceElementName)) {
                    node.add(elapsedTime, removeLastElement(stackTraceElementsList));
                    found = true;
                }
            }

            if (!found) {
                TreeNode newNode = new TreeNode(stackTraceElementName);
                newNode.add(elapsedTime, removeLastElement(stackTraceElementsList));
                nodes.add(newNode);
            }
        }
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

    abstract public void clear();

    private String getStackTraceElementName(StackTraceElement stackTraceElement) {
        return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
    }

    private List removeLastElement(List list) {
        return list.subList(0, list.size() - 1);
    }
}
