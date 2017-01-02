package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

public interface Sender {
    void sendMessage(String message) throws UnableToSendMessageException;
}
