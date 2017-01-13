package pl.com.tokarzewski.arduinomessenger.connection;

import java.util.Stack;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Kamil on 2017-01-12.
 */
public class SynchronousDataListener implements IncomingDataListener {
    private final SynchronousQueue<String> queue = new SynchronousQueue<>();

    @Override
    public void onNewIncomingData(String data) {
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getData() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "";
        }
    }
}
