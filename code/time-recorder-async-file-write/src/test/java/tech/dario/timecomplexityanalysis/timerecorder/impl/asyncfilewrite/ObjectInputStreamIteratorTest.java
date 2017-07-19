
package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite;

import org.junit.Test;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.model.MethodAction;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.model.MethodFinished;
import tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite.model.MethodStarted;

import java.io.*;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.*;

public class ObjectInputStreamIteratorTest {
  @Test
  public void test() throws Exception {
    final URL resource = getClass().getClassLoader().getResource("method-actions.ser");
    final URI fileUri = resource.toURI();
    final File file = new File(fileUri);
    try (
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis)
    ) {
      ObjectInputStreamIterator<MethodAction> iterator = new ObjectInputStreamIterator<>(ois);

      int i = 0;
      while (iterator.hasNext()) {
        MethodAction next = iterator.next();
        if (i == 0) {
          assertEquals(new MethodStarted("test", 126453670389743L), next);
        } else if(i == 1) {
          assertEquals(new MethodFinished("test", 126453671080124L), next);
        }

        i++;
      }

      assertEquals(2, i);
    }
  }
}
