package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.ResponseHandleException;
import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

import java.io.IOException;
import java.util.Observer;

public interface Connection {
    void setUpAndConnect() throws IOException;

    void tearDownAndDisconnect() throws IOException;

    void addResponseObserver(Observer observer);

    boolean isConnected();

    String getResponse() throws ResponseHandleException;

    void sendMessage(String msg) throws UnableToSendMessageException;
    //Sender getSender();

}
