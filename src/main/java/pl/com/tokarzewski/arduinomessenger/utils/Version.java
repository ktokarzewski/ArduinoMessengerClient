package pl.com.tokarzewski.arduinomessenger.utils;

public class Version {
    private static final String CURRENT_NUMBER = "0.1.2";
    private static final String CODE_NAME = "Mini";

    public static String getNumber() {
        return CURRENT_NUMBER;
    }

    public static String getCodeName() {
        return CODE_NAME;
    }

    static String getCompleteVersionString() {
        return CURRENT_NUMBER + "/" + CODE_NAME;
    }

    @Override
    public String toString() {
        return getCompleteVersionString();
    }
}
