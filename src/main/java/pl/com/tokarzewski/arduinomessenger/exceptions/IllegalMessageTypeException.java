package pl.com.tokarzewski.arduinomessenger.exceptions;

/**
 * Created by Kamil on 2017-01-11.
 */
public class IllegalMessageTypeException extends MessageDeserializerException {
    public IllegalMessageTypeException(String message) {
        super("Unsupported message type: %s", message);
    }
}
