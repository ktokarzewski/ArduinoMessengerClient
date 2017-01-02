package pl.com.tokarzewski.arduinomessenger;

import pl.com.tokarzewski.arduinomessenger.messages.Message;

public interface MessageListener {
    void onNewMessage(Message message);
}
