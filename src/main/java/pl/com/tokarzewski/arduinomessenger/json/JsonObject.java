package pl.com.tokarzewski.arduinomessenger.json;

public abstract class JsonObject {
    @Override
    public String toString() {
        return JsonParser.getParser().toJsonString(this);
    }
}
