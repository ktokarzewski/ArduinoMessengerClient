package pl.com.tokarzewski.arduinomessenger.connection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.com.tokarzewski.arduinomessenger.exceptions.ConnectionClosedByServerException;

import java.net.BindException;
import java.net.ConnectException;
import java.nio.channels.UnresolvedAddressException;
import java.util.Stack;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

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
        connection.setupAndConnect();

        //then
        assertThat(connection.isConnected()).isTrue();
    }

    @Test
    public void shouldNotifyIfConnected() throws Exception {
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();
        ConnectionImpl connection = new ConnectionImpl(socket);
        final Stack<Boolean> statusWrapper = new Stack<>();
        ConnectionStatusListener listener = new ConnectionStatusListener() {
            @Override
            public void connectionStatusChanged(boolean connected) {
                statusWrapper.push(connected);
            }
        };
        connection.addConnectionStatusListener(listener);

        assertThat(statusWrapper).isEmpty();
        connection.setupAndConnect();

        assertThat(statusWrapper.pop()).isTrue();
        assertThat(statusWrapper).isEmpty();

    }

    @Test
    public void shouldNotifyIfDisconnectedByServer() throws Exception {
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true)
                .willReturn(true)
                .willReturn(false)
                .given(socket)
                .isConnected();

        willThrow(new ConnectionClosedByServerException()).given(socket).read();

        ConnectionImpl connection = new ConnectionImpl(socket);

        BlockingStatusListener listener = new BlockingStatusListener();
        connection.addConnectionStatusListener(listener);
        connection.setupAndConnect();
        assertThat(listener.getStatus()).isTrue();

        assertThat(listener.getStatus()).isFalse();

        verify(socket).read();
        verify(socket, times(3)).isConnected();
        connection.tearDownAndDisconnect();

    }
    @Ignore
    @Test
    public void shouldThrowConnectException() throws Exception {
        //given
        int correctPort = 5544;
        Connection connection = new ConnectionImpl("localhost", correctPort);

        when(connection).setupAndConnect();

        //then
        assertThat(caughtException()).isInstanceOf(ConnectException.class);
    }

    @Test
    public void shouldThrowUnresolvedAddressException() throws Exception {
        //given
        String incorrectHostName = "wrong host";
        Connection connection = new ConnectionImpl(incorrectHostName, correctPort);

        when(connection).setupAndConnect();

        //then
        assertThat(caughtException()).isInstanceOf(UnresolvedAddressException.class);
    }

    @Test
    public void shouldThrowBindException() throws Exception {
        //given
        int incorrecPort = 0;
        Connection connection = new ConnectionImpl(correctHostName, incorrecPort);

        when(connection).setupAndConnect();

        //then
        assertThat(caughtException()).isInstanceOf(BindException.class);
    }

    @Ignore
    @Test
    public void shouldThrowConnectExceptionAfterTimeout() throws Exception {
        //given
        String hostName = "192.168.1.12";
        Connection connection = new ConnectionImpl(hostName, correctPort);

        when(connection).setupAndConnect();

        //then
        assertThat(caughtException()).isInstanceOf(ConnectException.class);
    }

    @Test
    public void shouldReadFromSocket() throws Exception {
        //given
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();
        String messageExample = "message;";


        willReturn(messageExample).willReturn("new message").given(socket).read();

        ConnectionImpl connection = new ConnectionImpl(socket);
        SynchronousDataListener listener = new SynchronousDataListener();
        connection.addIncomingDataListener(listener);

        //when
        connection.setupAndConnect();


        //then
        assertThat(listener.getData()).isEqualTo(messageExample);
    }
}