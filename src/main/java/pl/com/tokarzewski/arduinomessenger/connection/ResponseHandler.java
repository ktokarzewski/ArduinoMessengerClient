package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.ResponseHandleException;

public interface ResponseHandler extends Runnable {
    String getResponse() throws ResponseHandleException;

    boolean isReadyToRead();

    void startReadThread();

    void interruptReadThread();


}
