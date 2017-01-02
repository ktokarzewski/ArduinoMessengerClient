package pl.com.tokarzewski.arduinomessenger.connection;


import org.junit.Before;
import org.junit.Test;
import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SenderImplTest {

    private String message;

    @Before
    public void setUp() throws Exception {

        message = "Random message";

    }

    @Test
    public void shouldThrowUnableToSendMessageException() throws Exception {
        //given
        SocketDAO socket = mock(SocketDAO.class);
        willThrow(IOException.class).given(socket).write(anyString());

        Sender sender = new SenderImpl(socket);

        //when
        when(sender).sendMessage(message);

        //then

        assertThat(caughtException())
                .isInstanceOf(UnableToSendMessageException.class);
    }

    @Test
    public void shouldThrowUnableToSendMessageExceptionWhenSocketIsNotConnected() throws Exception {
        //given
        SocketDAO socket = mock(SocketDAO.class);
        willThrow(NotYetConnectedException.class).given(socket).write(anyString());

        Sender sender = new SenderImpl(socket);

        //when
        when(sender).sendMessage(message);

        //then

        assertThat(caughtException())
                .isInstanceOf(UnableToSendMessageException.class);
    }

    @Test
    public void shouldSendGivenMessage() throws Exception {
        //given
        SocketDAO socket = mock(SocketDAO.class);
        Sender sender = new SenderImpl(socket);
        //when
        sender.sendMessage(message);
        //then

        verify(socket).write(message);
    }
}