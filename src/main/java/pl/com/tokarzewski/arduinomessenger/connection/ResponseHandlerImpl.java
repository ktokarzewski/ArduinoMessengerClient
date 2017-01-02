package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.ResponseHandleException;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class ResponseHandlerImpl extends Observable implements ResponseHandler {
    static final String THREAD_NAME = "ResponseHandlerImpl";
    private static final String END_OF_MESSAGE = ";";
    private BlockingQueue<String> responseQueue;
    private SocketDAO socket;
    private Thread readThread;
    private String response;
    private StringBuilder buffer;
    private boolean ready;

    public ResponseHandlerImpl(SocketDAO socket) {
        this.socket = socket;
        response = "";
        buffer = new StringBuilder();
        responseQueue = new ArrayBlockingQueue<>(128);
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
            putMessageInQueue();
            setChangedAndNotifyObservers();
        }
    }


    private void putMessageInQueue() {
        try {
            responseQueue.put(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            interruptReadThread();
            e.printStackTrace();
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
        notifyObservers();
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

    @Override
    public String getResponse() throws ResponseHandleException {
        try {
            return responseQueue.take();
        } catch (InterruptedException e) {
            throw new ResponseHandleException("Method shouldn't be called outside update() method of observer class", e);
        }
    }


}
