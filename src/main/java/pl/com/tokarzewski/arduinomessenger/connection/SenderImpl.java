package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;


class SenderImpl implements Sender {
    private SocketDAO socket;
    private String message;

    SenderImpl(SocketDAO socket) {
        this.socket = socket;
    }

    @Override
    public void sendMessage(String message) throws UnableToSendMessageException {
        this.message = message;
        writeToSocket();
    }


    private void writeToSocket() throws UnableToSendMessageException {
        try {
            socket.write(message);
        } catch (IOException | NotYetConnectedException e) {
            throw new UnableToSendMessageException(e);
        }
    }


}
