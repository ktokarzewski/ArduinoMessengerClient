package pl.com.tokarzewski.arduinomessenger;

import pl.com.tokarzewski.arduinomessenger.connection.Connection;
import pl.com.tokarzewski.arduinomessenger.exceptions.MessageDeserializerException;
import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;
import pl.com.tokarzewski.arduinomessenger.messages.*;

import java.util.ArrayList;
import java.util.List;


public class MessengerImpl implements Messenger {

    private MessageDeserializer deserializer;
    private Connection connection;
    private Message message;
    private List<MessageListener> listeners;


    public MessengerImpl(Connection c) {
        this.connection = c;
        this.deserializer = new MessageDeserializer();
        this.connection.addIncomingDataListener(this);
        this.listeners = new ArrayList<>();
    }

    @Override
    public void sendGetMessage(String request) {
        sendBySender(new GetMessage(request));
    }

    @Override
    public void sendPutMessage(String resourceName, String value) {
        sendBySender(new PutMessage(resourceName, value));
    }

    @Override
    public void sendHelloMessage() {
        sendBySender(new HelloMessage());
    }

    @Override
    public void addIncomingMessageListener(MessageListener listener) {
        listeners.add(listener);
    }

    private void sendBySender(Message message) {
        String messageString = message.toString();
        try {
            connection.sendMessage(messageString);
        } catch (UnableToSendMessageException e) {
            e.printStackTrace();
        }
    }


    private void notifyListeners() {
        for (MessageListener listener : listeners) {
            listener.onNewMessage(message);
        }
    }

    private void deserializeResponse(String response) {
        try {
            message = (Message) deserializer.deserializeMessage(response);
        } catch (MessageDeserializerException e) {
            message = new ErrorMessage(e.getRawMessage());
        }
    }

    @Override
    public void onNewIncomingData(String data) {
        deserializeResponse(data);
        notifyListeners();
    }
}
