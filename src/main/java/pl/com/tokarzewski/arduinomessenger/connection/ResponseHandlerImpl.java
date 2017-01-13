package pl.com.tokarzewski.arduinomessenger.connection;

import java.io.IOException;

public class ResponseHandlerImpl extends ObservableResponseHandler {
    static final String THREAD_NAME = "ResponseHandlerImpl";
    private static final String END_OF_MESSAGE = ";";
    private SocketDAO socket;
    private Thread readThread;
    private String response;
    private StringBuilder buffer;
    private boolean ready;

    public ResponseHandlerImpl(SocketDAO socket) {
        this.socket = socket;
        response = "";
        buffer = new StringBuilder();
        ready = false;
    }

    public void startReadThread() {
        ready = true;
        readThread = new Thread(this);
        readThread.setName(THREAD_NAME);
        readThread.start();
    }

    @Override
    public void run() {
        while (isReadyToRead()) {
            readFromSocket();
            if (isReadyToRead()) {
                setChangedAndNotifyObservers();
            }
        }
    }


    private void readFromSocket() {
        try {
            while (!responseContainsEndOfMessageCharSequence()) {
                String read = socket.read();
                buffer.append(read);
            }
            extractResponse();

        } catch (IOException e) {
            setChanged();
            notifyObservers(e);
            interruptReadThread();
        }
    }

    private void extractResponse() {
        int indexOfEndOfMessage = buffer.indexOf(END_OF_MESSAGE) + 1;
        response = buffer.substring(0, indexOfEndOfMessage);
        buffer = new StringBuilder(buffer.subSequence(indexOfEndOfMessage, buffer.length()));
    }

    private boolean responseContainsEndOfMessageCharSequence() {
        return buffer.indexOf(END_OF_MESSAGE) > 0;
    }

    private void setChangedAndNotifyObservers() {
        setChanged();
        notifyObservers(response);
    }

    @Override
    public boolean isReadyToRead() {
        return ready && socket.isConnected() && !readThread.isInterrupted();
    }

    public void interruptReadThread() {
        if (ready)
            readThread.interrupt();
        ready = false;
    }


}
