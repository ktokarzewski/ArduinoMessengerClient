package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

import java.io.IOException;

class ThreadSenderImpl implements Sender, Runnable {
    private static final String THREAD_NAME = "ThreadSenderImpl";
    private SocketDAO socket;
    private String message;

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
            e.printStackTrace();
        }
    }
}
