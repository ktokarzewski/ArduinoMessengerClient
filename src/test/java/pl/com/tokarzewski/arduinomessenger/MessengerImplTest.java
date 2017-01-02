package pl.com.tokarzewski.arduinomessenger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.com.tokarzewski.arduinomessenger.connection.Connection;
import pl.com.tokarzewski.arduinomessenger.connection.ConnectionImpl;
import pl.com.tokarzewski.arduinomessenger.messages.GetMessage;
import pl.com.tokarzewski.arduinomessenger.messages.HelloMessage;
import pl.com.tokarzewski.arduinomessenger.messages.Message;
import pl.com.tokarzewski.arduinomessenger.messages.SendMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;


public class MessengerImplTest {


    private String message;
    private Connection connection;
    private Messenger messenger;
    private String color;


    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        message = "banana";
        color = "color";
        messenger = new MessengerImpl(connection);

    }

    @Test
    public void shouldSendRequestMessage() throws Exception {
        //given
        GetMessage getMessage = new GetMessage(message);
        //when
        messenger.sendRequestMessage(message);
        //then
        verify(connection, times(1)).sendMessage(getMessage.toString());
    }


    @Test
    public void shouldSendResponseMessage() throws Exception {
        //given
        SendMessage sendMessage = new SendMessage(message, color);

        //when
        messenger.sendResponseMessage(message, color);

        //then
        verify(connection, times(1)).sendMessage(sendMessage.toString());
    }

    @Test
    public void shouldSendHelloMessage() throws Exception {
        //given
        HelloMessage hello = new HelloMessage();

        //when
        messenger.sendHelloMessage();

        //then
        verify(connection, times(1)).sendMessage(hello.toString());
    }

    @Test
    public void shouldReceiveResponse() throws Exception {

        final List<Message> messageWraper = new ArrayList<>(1);
        MessageListener messageListener = new MessageListener() {

            @Override
            public void onNewMessage(Message message) {
                messageWraper.add(message);
            }

        };
        //given
        String response = "GET\n" +
                "{\"request\":\"banana\",\"id\":\"Kamil\"};";
        messenger.addIncomingMessageListener(messageListener);


        willReturn(response).given(connection).getResponse();

        //when
        messenger.update(null, null);

        //then

        assertThat(messageWraper.get(0).toString()).isEqualTo(response);

    }


    @Ignore
    @Test
    public void shouldCommunicateWithArduino() throws Exception {
        Connection c = new ConnectionImpl("192.168.1.10", 5544);
        Messenger m = new MessengerImpl(c);
        final Queue<Message> messageQueue = new LinkedList<>();
        MessageListener handler = new MessageListener() {
            @Override
            public void onNewMessage(Message message) {
                messageQueue.add(message);
                System.out.println("new Message");
            }
        };
        m.addIncomingMessageListener(handler);
        c.setUpAndConnect();
        m.sendRequestMessage("temperature");
        Thread.sleep(100);
        m.sendRequestMessage("temperature");
        Thread.sleep(100);
        m.sendRequestMessage("temperature");
        Thread.sleep(100);

        assertThat(c.isConnected()).isTrue();
        while (messageQueue.size() < 3) ;

        while (messageQueue.size() > 0)
            System.out.println(messageQueue.poll());

    }
}