package pl.com.tokarzewski.arduinomessenger.connection;

import java.io.IOException;


public interface SocketDAO {
    void write(String message) throws IOException;

    boolean isConnected();

    String read() throws IOException;

    boolean connect() throws IOException;

    boolean disconnect() throws IOException;
}
