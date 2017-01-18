package pl.com.tokarzewski.arduinomessenger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.com.tokarzewski.arduinomessenger.connection.Connection;
import pl.com.tokarzewski.arduinomessenger.connection.ConnectionImpl;
import pl.com.tokarzewski.arduinomessenger.connection.ResponseHandler;
import pl.com.tokarzewski.arduinomessenger.connection.SocketDAO;
import pl.com.tokarzewski.arduinomessenger.messages.GetMessage;
import pl.com.tokarzewski.arduinomessenger.messages.HelloMessage;
import pl.com.tokarzewski.arduinomessenger.messages.Message;
import pl.com.tokarzewski.arduinomessenger.messages.PutMessage;

import java.util.*;

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
        messenger.sendGetMessage(message);
        //then
        verify(connection, times(1)).sendMessage(getMessage.toString());
    }


    @Test
    public void shouldSendResponseMessage() throws Exception {
        //given
        PutMessage putMessage = new PutMessage(message, color);

        //when
        messenger.sendPutMessage(message, color);

        //then
        verify(connection, times(1)).sendMessage(putMessage.toString());
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

}