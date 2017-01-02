package pl.com.tokarzewski.arduinomessenger.messages;

import pl.com.tokarzewski.arduinomessenger.json.JsonParser;

public class MessageSerializer {
    public String serializeFrame(ProtocolFrame frame) {
        return JsonParser.getParser().toJsonString(frame);
    }
}
