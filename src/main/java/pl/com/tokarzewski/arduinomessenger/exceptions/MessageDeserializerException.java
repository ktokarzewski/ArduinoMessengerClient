package pl.com.tokarzewski.arduinomessenger.exceptions;

/**
 * Created by Kamil on 2017-01-11.
 */
public class MessageDeserializerException extends Exception {
    private String rawMessage;

    public MessageDeserializerException(String format, String message) {
        super(String.format(format, message));
        rawMessage = message;
    }

    public String getRawMessage() {
        return rawMessage;
    }
}
