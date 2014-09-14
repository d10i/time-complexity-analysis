package ch.satoshi.dissertation.agent.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RootNode extends Node {

    public static RootNode deserializeFromFile(File file) throws IOException {
        return new ObjectMapper().readValue(file, RootNode.class);
    }

    @JsonIgnore
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void clear() {
        this.nodes.clear();
    }
}
