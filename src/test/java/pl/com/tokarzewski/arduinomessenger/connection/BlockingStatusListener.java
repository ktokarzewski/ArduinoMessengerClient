package pl.com.tokarzewski.arduinomessenger.connection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Kamil on 2017-01-13.
 */
class BlockingStatusListener implements ConnectionStatusListener {
    private final BlockingQueue<Boolean> queue = new ArrayBlockingQueue<>(1);

    @Override
    public void connectionStatusChanged(boolean connected) {
        try {
            queue.put(connected);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Boolean getStatus() {
        try {
            return queue.take();
        } catch (InterruptedException ignored) {
            return null;
        }
    }
}
