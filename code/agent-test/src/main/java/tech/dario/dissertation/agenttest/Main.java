package tech.dario.dissertation.agenttest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.agent.annotations.Measured;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private static final Executor1 EXECUTOR = new Executor1();

  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    LOGGER.info("Running agent-test with n: {}", n);
    doTask(n);
    LOGGER.info("Finished running agent-test with n: {}", n);
  }

  @Measured
  private static void doTask(int n) {
    EXECUTOR.execute(n);
  }
}
