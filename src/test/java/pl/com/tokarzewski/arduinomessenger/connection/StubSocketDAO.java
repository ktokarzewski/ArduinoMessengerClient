package pl.com.tokarzewski.arduinomessenger.connection;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class StubSocketDAO extends SocketDAOImpl {
    Queue<String> read;


    StubSocketDAO() {
        read = new LinkedList<>();
    }

    @Override
    public String read() throws IOException {
        if (read.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                //nothing to do here
            }
        }
        return read.poll();

    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
