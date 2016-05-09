package tech.dario.dissertation.timecomplexityanalyser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.agent.annotations.Measured;
import tech.dario.dissertation.timereporter.api.TimeReporter;

import java.io.IOException;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  private static final TimeReporter _AGENT_TIME_REPORTER = tech.dario.dissertation.timereporter.api.TimeReporterFactoryUtil.getTimeReporter(Main.class);

  public static void main(String[] args) throws IOException, InterruptedException {
    long start;
    String clazz = args[0];
    for (int i = 1; i <= 5; i++) {
      int n = Math.round(i * i * 1.3f);
      start = System.nanoTime();
      doTask(clazz, n);
      LOGGER.info("{}\t{}", n, String.format("%.4f", (System.nanoTime() - start) / 1000000000.0f));
    }
  }

  @Measured
  private static int doTask(String clazz, int n) throws IOException, InterruptedException {
    long _agent_startTime = System.nanoTime();
    LOGGER.info("doTask: class = {}, n = {}", clazz, n);
    ProcessBuilder pb = new ProcessBuilder("java", clazz, String.valueOf(n));
    pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
    pb.redirectError(ProcessBuilder.Redirect.PIPE);
    Process p = pb.start();
    int result = p.waitFor();
    LOGGER.info("Done doTask: class = {}, n = {}", clazz, n);
    LOGGER.info("Result: {}", result);
        /*if(result != 0) {
            System.exit(result);
        }*/
    _AGENT_TIME_REPORTER.reportTime(System.nanoTime() - _agent_startTime, Thread.currentThread());
    return result;
  }
}
