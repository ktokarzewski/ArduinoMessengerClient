package pl.com.tokarzewski.arduinomessenger.connection;


public interface ResponseHandler extends Runnable {
    boolean isReadyToRead();

    void startReadThread();

    void interruptReadThread();
}
