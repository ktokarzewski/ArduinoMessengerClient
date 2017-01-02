package pl.com.tokarzewski.arduinomessenger;


import java.util.Observer;

public interface Messenger extends Observer {
    void sendRequestMessage(String request);

    void sendResponseMessage(String resourceName, String value);

    void sendHelloMessage();

    void addIncomingMessageListener(MessageListener listener);
}
