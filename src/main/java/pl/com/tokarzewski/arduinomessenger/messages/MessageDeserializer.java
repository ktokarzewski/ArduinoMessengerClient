/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.tokarzewski.arduinomessenger.messages;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import pl.com.tokarzewski.arduinomessenger.exceptions.IllegalMessageTypeException;
import pl.com.tokarzewski.arduinomessenger.exceptions.InvalidMessageFormatException;
import pl.com.tokarzewski.arduinomessenger.exceptions.MessageDeserializerException;
import pl.com.tokarzewski.arduinomessenger.json.JsonParser;

import java.util.Iterator;

public class MessageDeserializer implements FrameDeserializer {
    public static final int SPLIT_SIZE = 2;

    private String type;
    private JsonParser parser;
    private String content;
    private ProtocolFrame frame;

    public MessageDeserializer() {
        parser = JsonParser.getParser();
    }

    private void extractContentAndType(String data) throws MessageDeserializerException {
        CharMatcher trimMatcher = CharMatcher.anyOf(" ;");
        Iterable<String> split = Splitter.on("\n")
                .trimResults(trimMatcher)
                .split(data);
        validateRawMessage(data, split);
        Iterator<String> it = split.iterator();
        setAndValidateType(it.next().trim());
        content = it.next().trim();
    }

    private void validateRawMessage(String message, Iterable<String> split) throws InvalidMessageFormatException {
        if (Iterables.size(split) != SPLIT_SIZE) {
            throw new InvalidMessageFormatException(message);
        }
    }

    private void setAndValidateType(String type) throws MessageDeserializerException {
        if (messageIsValid(type))
            this.type = type;
    }

    @Override
    public ProtocolFrame deserializeMessage(String message) throws MessageDeserializerException {
        extractContentAndType(message);
        deserializeFrame();
        return this.frame;
    }

    private void deserializeFrame() throws IllegalArgumentException {
        try {
            frame = parser.fromJson(content, type);
        } catch (JsonSyntaxException | JsonIOException e) {
            e.addSuppressed(new IllegalArgumentException("Received message: ".concat(content)));
            throw e;
        }
    }

    private boolean messageIsValid(String type) throws MessageDeserializerException {
        if (!(MessageTypes.getValidTypeList().contains(type))) {
            throw new IllegalMessageTypeException(type);
        } else return true;
    }


}
