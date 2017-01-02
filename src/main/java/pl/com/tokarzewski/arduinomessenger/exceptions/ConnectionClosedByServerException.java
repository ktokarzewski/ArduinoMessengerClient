package pl.com.tokarzewski.arduinomessenger.exceptions;

import java.io.IOException;

public class ConnectionClosedByServerException extends IOException {
    public ConnectionClosedByServerException(String message) {
        super(message);
    }

    public ConnectionClosedByServerException() {
        super();
    }
}
