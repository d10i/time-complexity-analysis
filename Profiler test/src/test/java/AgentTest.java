import example.Main;
import org.junit.Test;

public class AgentTest {
    @Test
    public void testPremain() throws Exception {
        String[] args = {"1"};
        Main.main(args);
        System.out.println(new LoggerSingleton().get().tree);
    }
}