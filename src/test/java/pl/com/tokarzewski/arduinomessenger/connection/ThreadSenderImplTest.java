package pl.com.tokarzewski.arduinomessenger.connection;


import org.junit.Ignore;
import org.junit.Test;
import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

import java.io.IOException;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Ignore
public class ThreadSenderImplTest {
    @Test
    public void shouldSendMessage() throws Exception {
        //given
        String message = "message";
        SocketDAO mock = mock(SocketDAO.class);
        ThreadSenderImpl threadSender = new ThreadSenderImpl(mock);

        //when
        threadSender.sendMessage(message);

        //then
        verify(mock).write(message);
    }


    @Test
    public void shouldThrowException() throws Exception {
        //given
        String message = "message";
        SocketDAO mock = mock(SocketDAO.class);
        ThreadSenderImpl threadSender = new ThreadSenderImpl(mock);
        willThrow(IOException.class).given(mock).write(message);

        //when
        when(threadSender).sendMessage("message");

        //then
        assertThat(caughtException()).isInstanceOf(UnableToSendMessageException.class);


    }
}