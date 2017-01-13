package pl.com.tokarzewski.arduinomessenger.connection;


import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ThreadSenderImplTest {
    @Ignore
    @Test
    public void shouldSendMessage() throws Exception {
        //given
        String message = "message";
        SocketDAO mock = mock(SocketDAO.class);
        ThreadSenderImpl threadSender = new ThreadSenderImpl(mock);

        //when
        threadSender.sendMessage(message);
        Thread.sleep(100);

        //then
        verify(mock).write(message);
    }



}