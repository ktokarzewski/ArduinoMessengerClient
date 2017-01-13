package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

import java.io.IOException;

public interface Connection {
    void setupAndConnect() throws IOException;

    void tearDownAndDisconnect() throws IOException;


    boolean isConnected();


    void sendMessage(String msg) throws UnableToSendMessageException;


    void addIncomingDataListener(IncomingDataListener messenger);
}
