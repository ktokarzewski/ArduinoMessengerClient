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

    private void extractContentAndType(String data) {
        CharMatcher trimMatcher = CharMatcher.anyOf(" ;");
        Iterable<String> split = Splitter.on("\n")
                .trimResults(trimMatcher)
                .split(data);
        validateRawMessage(data, split);
        Iterator<String> it = split.iterator();
        setAndValidateType(it.next().trim());
        content = it.next().trim();
    }

    private void validateRawMessage(String message, Iterable<String> split) {
        if (Iterables.size(split) != SPLIT_SIZE) {
            throw new IllegalArgumentException(message.concat(" is not correct frame content"));
        }
    }

    private void setAndValidateType(String type) {
        if (messageIsValid(type))
            this.type = type;
    }

    @Override
    public ProtocolFrame deserializeMessage(String message) throws IllegalArgumentException {
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

    private boolean messageIsValid(String type) throws IllegalArgumentException {
        if (!(MessageTypes.getValidTypeList().contains(type))) {
            throw new IllegalArgumentException("Unsupported message type: ".concat(type));
        } else return true;
    }


}
