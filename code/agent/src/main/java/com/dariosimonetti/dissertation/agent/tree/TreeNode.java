package com.dariosimonetti.dissertation.agent.tree;

import com.dariosimonetti.dissertation.agent.MeasuredStackTraceElements;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TreeNode<T extends MergeableValue> extends Node<T> {
    @JsonProperty
    private String name;

    @JsonProperty
    private T value;

    private TreeNode() {
    }

    public TreeNode(String name) {
        this.name = name;
        clear();
    }

    @Override
    public Node<T> add(T value, MeasuredStackTraceElements stackTraceElementsList) {
        if (stackTraceElementsList.size() > 0) {
            return super.add(value, stackTraceElementsList);
        } else {
            updateValue(value);
            return this;
        }
    }

    private void updateValue(T value) {
        if (this.value != null) {
            this.value = (T) this.value.mergeWith(value);
        } else {
            this.value = value;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void clear() {
        super.clear();
        this.value = null;
    }
}
