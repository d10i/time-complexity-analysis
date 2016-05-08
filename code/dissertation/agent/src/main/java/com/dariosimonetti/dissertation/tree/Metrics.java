package com.dariosimonetti.dissertation.tree;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metrics implements MergeableValue {
    @JsonProperty
    private long count;

    @JsonProperty
    private double total;

    @JsonProperty
    private double min;

    @JsonProperty
    private double max;

    private Metrics() {
    }

    public Metrics(long count, double total, double min, double max) {
        this.count = count;
        this.total = total;
        this.min = min;
        this.max = max;
    }

    public static Metrics fromElapsedTime(long elapsedTime) {
        return new Metrics(1, elapsedTime, elapsedTime, elapsedTime);
    }

    @Override
    public MergeableValue mergeWith(MergeableValue mergeableValue) {
        if (mergeableValue instanceof Metrics) {
            Metrics metrics = (Metrics) mergeableValue;
            this.count += metrics.count;
            this.total += metrics.total;
            this.min = Math.min(this.min, metrics.min);
            this.max = Math.max(this.max, metrics.max);
            return this;
        }

        throw new IllegalArgumentException("Can't merge Metrics object with a non-Metrics object");
    }
}
