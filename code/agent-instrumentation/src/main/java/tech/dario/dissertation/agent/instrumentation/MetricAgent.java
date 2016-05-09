package tech.dario.dissertation.agent.instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class MetricAgent {

  private final static Logger LOGGER = LoggerFactory.getLogger(MetricAgent.class);

  public static void premain(String agentArguments, Instrumentation instrumentation) {
    RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
    LOGGER.info("Runtime: {}: {}", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());
    LOGGER.info("Starting agent with arguments " + agentArguments);
    instrumentation.addTransformer(new MeasuredClassTransformer());
  }
}