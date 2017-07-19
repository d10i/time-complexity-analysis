package tech.dario.timecomplexityanalysis.timerecorder.impl.asyncfilewrite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

public class ObjectInputStreamIterator<T> implements Iterator<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ObjectInputStreamIterator.class);

  private final ObjectInputStream objectInputStream;
  private T next;

  public ObjectInputStreamIterator(ObjectInputStream objectInputStream) {
    this.objectInputStream = objectInputStream;
    this.next = readNext();
  }

  @Override
  public boolean hasNext() {
    return next != null;
  }

  @Override
  public T next() {
    T toReturn = next;
    next = readNext();
    return toReturn;
  }

  private T readNext() {
    try {
      return (T) objectInputStream.readObject();
    } catch (EOFException ignored) {

    } catch (IOException | ClassNotFoundException e) {
      LOGGER.error("Unexpected exception", e);
      System.exit(1);
    }

    return null;
  }
}
