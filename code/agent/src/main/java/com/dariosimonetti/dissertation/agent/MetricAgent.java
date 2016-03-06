package com.dariosimonetti.dissertation.agent;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricAgent {
	private final static Logger logger = LoggerFactory.getLogger(MetricAgent.class);

    public static void premain(String agentArguments, Instrumentation instrumentation) {
    	RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
    	logger.info("Runtime: {}: {}", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());
        logger.info("Starting agent with arguments " + agentArguments);
        instrumentation.addTransformer(new TimedClassTransformer());
    }
}