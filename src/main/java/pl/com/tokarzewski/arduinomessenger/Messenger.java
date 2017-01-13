package pl.com.tokarzewski.arduinomessenger;


import pl.com.tokarzewski.arduinomessenger.connection.IncomingDataListener;

public interface Messenger extends IncomingDataListener {
    void sendGetMessage(String request);

    void sendPutMessage(String resourceName, String value);

    void sendHelloMessage();

    void addIncomingMessageListener(MessageListener listener);
}
