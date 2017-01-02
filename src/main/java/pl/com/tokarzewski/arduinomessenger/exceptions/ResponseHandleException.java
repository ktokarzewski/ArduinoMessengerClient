package pl.com.tokarzewski.arduinomessenger.exceptions;

import java.io.IOException;

public class ResponseHandleException extends IOException {
    public ResponseHandleException(String message, Throwable cause) {
        super(message, cause);
    }
}
