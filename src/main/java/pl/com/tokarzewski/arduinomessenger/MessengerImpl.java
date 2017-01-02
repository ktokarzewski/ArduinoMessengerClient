package pl.com.tokarzewski.arduinomessenger;

import pl.com.tokarzewski.arduinomessenger.connection.Connection;
import pl.com.tokarzewski.arduinomessenger.connection.ConnectionImpl;
import pl.com.tokarzewski.arduinomessenger.exceptions.ResponseHandleException;
import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;
import pl.com.tokarzewski.arduinomessenger.messages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class MessengerImpl implements Messenger {

    private MessageDeserializer deserializer;
    private Connection connection;
    private String response;
    private Message message;
    private List<MessageListener> listeners;

    public MessengerImpl(String serverHost, int serverPort) {
        this(new ConnectionImpl(serverHost, serverPort));
    }


    public MessengerImpl(Connection c) {
        this.connection = c;
        this.deserializer = new MessageDeserializer();
        this.connection.addResponseObserver(this);
        this.listeners = new ArrayList<>();
    }

    @Override
    public void sendRequestMessage(String request) {
        sendBySender(new GetMessage(request));
        //logger.info("GetMessage sent");
    }

    @Override
    public void sendResponseMessage(String resourceName, String value) {
        sendBySender(new SendMessage(resourceName, value));
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
            e.printStackTrace();    //
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        getResponse();
        deserializeResponse();
        notifyListeners();
    }

    private void notifyListeners() {
        for (MessageListener listener : listeners) {
            listener.onNewMessage(message);
        }
    }

    private void deserializeResponse() {
        message = (Message) deserializer.deserializeMessage(response);
    }

    private void getResponse() {
        try {
            response = connection.getResponse();
        } catch (ResponseHandleException e) {
            e.printStackTrace();
        }
    }

}
