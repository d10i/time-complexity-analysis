package com.dariosimonetti.dissertation.agent.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class TreeRoot<T extends MergeableValue> extends Node<T> {

    public static TreeRoot deserializeFromFile(File file) throws IOException {
        return new ObjectMapper().readValue(file, TreeRoot.class);
    }

    @JsonIgnore
    @Override
    public String getName() {
        return null;
    }
}
