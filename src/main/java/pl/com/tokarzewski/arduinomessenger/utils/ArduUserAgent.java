package pl.com.tokarzewski.arduinomessenger.utils;

import pl.com.tokarzewski.arduinomessenger.json.JsonObject;

public class ArduUserAgent extends JsonObject {
    private String version;
    private String os;

    public ArduUserAgent(String os, String version) {
        this.os = os;
        this.version = version;
    }

    public ArduUserAgent() {
        this.version = Version.getCompleteVersionString();
        this.os = getSystemNameAndVersion();
    }

    public static ArduUserAgent getDefaultUserAgent() {
        return new ArduUserAgent();
    }

    public String getOs() {
        return os;
    }

    public String getVersion() {
        return version;
    }

    private String getSystemNameAndVersion() {
        return System.getProperty("os.name") + "/" + System.getProperty("os.version");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ArduUserAgent) {
            ArduUserAgent userAgentObject = (ArduUserAgent) obj;
            return userAgentObject.os.equals(this.os) && userAgentObject.version.equals(this.version);
        } else
            return false;

    }


}