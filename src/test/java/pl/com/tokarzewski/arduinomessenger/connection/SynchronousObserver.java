package pl.com.tokarzewski.arduinomessenger.connection;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.SynchronousQueue;

/**
 * Simple Observer implementation for unit tests.
 */
public class SynchronousObserver implements Observer {
    private final SynchronousQueue<String> queue = new SynchronousQueue<>();

    @Override
    public void update(Observable o, Object arg) {
        try {
            queue.put((String) arg);
        } catch (InterruptedException ignore) {
        }
    }

    public String getData() {
        try {
            return queue.take();
        } catch (InterruptedException ignore) {
            return "";
        }
    }
}
