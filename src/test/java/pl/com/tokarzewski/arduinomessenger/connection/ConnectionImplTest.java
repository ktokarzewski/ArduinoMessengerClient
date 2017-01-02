package pl.com.tokarzewski.arduinomessenger.connection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.BindException;
import java.net.ConnectException;
import java.nio.channels.UnresolvedAddressException;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

public class ConnectionImplTest {

    private int correctPort;
    private String correctHostName;

    @Before
    public void setUp() throws Exception {
        correctHostName = "localhost";
        correctPort = 5544;
    }

    @Test
    public void shouldGetConnectionStatusWithSocketInjection() throws Exception {
        //given
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();
        Connection connection = new ConnectionImpl(socket);

        //when
        connection.setUpAndConnect();

        //then
        assertThat(connection.isConnected()).isTrue();
    }


    @Ignore
    @Test
    public void shouldThrowConnectException() throws Exception {
        //given
        int correctPort = 5544;
        Connection connection = new ConnectionImpl("localhost", correctPort);

        when(connection).setUpAndConnect();

        //then
        assertThat(caughtException()).isInstanceOf(ConnectException.class);
    }

    @Test
    public void shouldThrowUnresolvedAddressException() throws Exception {
        //given
        String incorrectHostName = "wrong host";
        Connection connection = new ConnectionImpl(incorrectHostName, correctPort);

        when(connection).setUpAndConnect();

        //then
        assertThat(caughtException()).isInstanceOf(UnresolvedAddressException.class);
    }

    @Test
    public void shouldThrowBindException() throws Exception {
        //given
        int incorrecPort = 0;
        Connection connection = new ConnectionImpl(correctHostName, incorrecPort);

        when(connection).setUpAndConnect();

        //then
        assertThat(caughtException()).isInstanceOf(BindException.class);
    }

    @Ignore
    @Test
    public void shouldThrowConnectExceptionAfterTimeout() throws Exception {
        //given
        String hostName = "192.168.1.12";
        Connection connection = new ConnectionImpl(hostName, correctPort);

        when(connection).setUpAndConnect();

        //then
        assertThat(caughtException()).isInstanceOf(ConnectException.class);
    }

    @Test(timeout = 100)
    public void shouldReadFromSocket() throws Exception {
        //given
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();
        String messageExample = "message;";
        willReturn(messageExample).given(socket).read();
        Connection connection = new ConnectionImpl(socket);

        //when
        connection.setUpAndConnect();


        //then
        assertThat(connection.getResponse()).isEqualTo(messageExample);
    }
}