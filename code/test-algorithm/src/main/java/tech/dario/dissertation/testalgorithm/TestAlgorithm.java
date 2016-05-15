package tech.dario.dissertation.testalgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.agent.annotations.Measured;

public class TestAlgorithm {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestAlgorithm.class);

  @Measured
  public void doTask(int n) {
    LOGGER.info("TestAlgorithm.doTask({})", n);
    new Executor1().execute(n);
  }
}
