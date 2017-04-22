package tech.dario.timecomplexityanalysis.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class MeasuringAgent {

  private final static Logger LOGGER = LoggerFactory.getLogger(MeasuringAgent.class);

  public static void premain(final String agentArguments, final Instrumentation instrumentation) {
    try {
      final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
      LOGGER.info("Runtime: {}: '{}'", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());
      LOGGER.info("Starting agent with arguments: '{}'", agentArguments);
      final Config config = getConfigFromArguments(agentArguments);
      instrumentation.addTransformer(new MeasuringClassFileTransformer(config));
    } catch (Exception e) {
      final String message = String.format("Unexpected exception in agent using arguments: '%s'", agentArguments);
      LOGGER.error(message, e);
      System.exit(1);
    }
  }

  private static Config getConfigFromArguments(String agentArguments) throws IOException {
    if (agentArguments == null || agentArguments.trim().isEmpty()) {
      // No arguments
      return Config.getDefault();
    }

    return Config.fromJsonFilePath(agentArguments);
  }
}
