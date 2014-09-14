package com.dariosimonetti.dissertation.agent.tree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeNode extends Node {

    @JsonProperty
    private final String name;

    @JsonProperty
    private long count;

    @JsonProperty
    private double total;

    @JsonProperty
    private double min;

    @JsonProperty
    private double max;

    @JsonProperty
    protected final List<TreeNode> nodes = new ArrayList<TreeNode>();

    public TreeNode() {
        this(null);
    }

    public TreeNode(String name) {
        this.name = name;
        clear();
    }

    @Override
    public void add(long elapsedTime, List<StackTraceElement> stackTraceElementsList) {
        if (stackTraceElementsList.size() > 0) {
            super.add(elapsedTime, stackTraceElementsList);
        } else {
            this.count++;
            this.total += elapsedTime;
            this.min = Math.min(this.min, elapsedTime);
            this.max = Math.max(this.max, elapsedTime);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void clear() {
        this.nodes.clear();
        this.count = 0;
        this.total = 0;
        this.min = Long.MAX_VALUE;
        this.max = Long.MIN_VALUE;
    }
}
