package tech.dario.dissertation.timecomplexityanalyser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dario.dissertation.agent.annotations.Measured;

import java.io.IOException;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
    long start;
    String jarPath = args[0];
    for (int i = 1; i <= 10; i++) {
      int n = i * i * 2;
      start = System.nanoTime();
      doTask(jarPath, n);
      LOGGER.info("{}\t{}", n, String.format("%.4f", (System.nanoTime() - start) / 1000000000.0f));
    }
  }

  @Measured
  private static int doTask(String jarPath, int n) throws IOException, InterruptedException, ClassNotFoundException {
    LOGGER.info("doTask: jar file = {}, n = {}", jarPath, n);
    ProcessBuilder pb = new ProcessBuilder().inheritIO().command("java", "-jar", jarPath, String.valueOf(n));
    Process p = pb.start();
    int result = p.waitFor();
    LOGGER.info("Done doTask: class = {}, n = {}", jarPath, n);
    LOGGER.info("Result: {}", result);
    if(result != 0) {
        System.exit(result);
    }
    return result;
  }
}
