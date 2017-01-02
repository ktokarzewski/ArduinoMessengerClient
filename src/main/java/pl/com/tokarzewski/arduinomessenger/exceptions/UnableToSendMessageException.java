package pl.com.tokarzewski.arduinomessenger.exceptions;

import java.io.IOException;

public class UnableToSendMessageException extends IOException {

    public UnableToSendMessageException(Exception e) {
        super(e);
    }
}
