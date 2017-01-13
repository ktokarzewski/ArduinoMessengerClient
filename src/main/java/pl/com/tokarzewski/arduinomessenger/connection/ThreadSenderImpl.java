package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;

class ThreadSenderImpl extends Observable implements Sender, Runnable {
    private static final String THREAD_NAME = "ThreadSenderImpl";
    private SocketDAO socket;
    private String message;
    private BlockingQueue<String> outputQueue;
    ThreadSenderImpl(SocketDAO socket) {
        this.socket = socket;
    }

    @Override
    public void sendMessage(String message) throws UnableToSendMessageException {
        this.message = message;
        startSendThread();
    }

    private void startSendThread() {
        Thread thread = new Thread(this);
        thread.setName(THREAD_NAME);
        thread.start();
    }

    @Override
    public void run() {
        send();
    }

    private void send() {
        try {
            socket.write(message);
        } catch (IOException e) {
            setChanged();
            notifyObservers(new UnableToSendMessageException(e));
        }
    }
}
