package pl.com.tokarzewski.arduinomessenger.exceptions;

/**
 * Created by Kamil on 2017-01-11.
 */
public class InvalidMessageFormatException extends MessageDeserializerException {

    public InvalidMessageFormatException(String message) {
        super("\"%s\" is not correct frame content", message);

    }

}
