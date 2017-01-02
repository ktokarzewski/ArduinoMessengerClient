package pl.com.tokarzewski.arduinomessenger.json;

import com.google.gson.*;
import pl.com.tokarzewski.arduinomessenger.utils.ArduUserAgent;

import java.lang.reflect.Type;

class UserAgentDeserializer implements JsonDeserializer<ArduUserAgent> {
    @Override
    public ArduUserAgent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement userAgent = json.getAsJsonObject().get("userAgent");
        return new Gson().fromJson(userAgent, ArduUserAgent.class);
    }
}
