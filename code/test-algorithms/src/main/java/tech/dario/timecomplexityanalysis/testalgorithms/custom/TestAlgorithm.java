package tech.dario.timecomplexityanalysis.testalgorithms.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAlgorithm {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestAlgorithm.class);

  public void doTask(long n) {
    LOGGER.info("TestAlgorithm.doTask({})", n);
    new Executor1().execute(n);
  }
}
