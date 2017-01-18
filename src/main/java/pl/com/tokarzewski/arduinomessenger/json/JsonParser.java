package pl.com.tokarzewski.arduinomessenger.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import pl.com.tokarzewski.arduinomessenger.messages.MessageTypes;

import java.io.StringReader;

public class JsonParser {
    private static JsonParser instance;
    private Gson gson;

    private JsonParser() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(UserAgentDeserializer.class, new UserAgentDeserializer())
                .setLenient()
                .create();
    }

    public static JsonParser getParser() {
        if (instance == null) {
            instance = new JsonParser();
        }
        return instance;
    }


    public <T> T fromJson(String content, Class classType) {
        JsonReader reader = setUpReader(content);
        return gson.fromJson(reader, classType);
    }

    public <T> T fromJson(String content, String type) {
        return fromJson(content, MessageTypes.getMessageClass(type));
    }

    private JsonReader setUpReader(String content) {
        JsonReader reader = new JsonReader(new StringReader(content));
        //   reader.setLenient(true);
        return reader;
    }

    public String toJsonString(Object object) {
        return gson.toJson(object);
    }
}
