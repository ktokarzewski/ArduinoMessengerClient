package pl.com.tokarzewski.arduinomessenger.connection;

import org.junit.Ignore;
import org.junit.Test;
import pl.com.tokarzewski.arduinomessenger.exceptions.ConnectionClosedByServerException;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;

public class SocketDAOImplTest {

    private String message = "banana";


    @Test
    public void shouldCheckIfConnectedWithEmptyConstructor() throws Exception {
        //given
        SocketDAO socket = new SocketDAOImpl();

        //then
        assertThat(socket.isConnected()).isFalse();

    }

    @Test
    public void shouldCheckIfConnectedWithHostPortConstructor() throws Exception {
        //given
        SocketDAO socket = new SocketDAOImpl("localhost", 5544);

        //then
        assertThat(socket.isConnected()).isFalse();

    }

    @Test
    public void shouldCheckIfConnectedWithSocketChannelConstructor() throws Exception {
        //given
        SocketDAO socket = new SocketDAOImpl(mock(SocketChannel.class));

        //then
        assertThat(socket.isConnected()).isFalse();

    }

    @Test
    public void shouldThrowNotYetConnectedException() throws Exception {
        SocketDAO socket = new SocketDAOImpl();

        //when
        when(socket).read();

        //then
        assertThat(caughtException()).isInstanceOf(NotYetConnectedException.class);
        assertThat(socket.isConnected()).isFalse();

    }

    @Test
    public void shouldThrowNotYetConnectedExceptionWhenWritingToSocket() throws Exception {
        SocketDAO socket = new SocketDAOImpl();

        //when
        when(socket).write("msg");

        //then
        assertThat(caughtException()).isInstanceOf(NotYetConnectedException.class);
        assertThat(socket.isConnected()).isFalse();


    }

    @Ignore
    @Test
    public void shouldWriteToChannel() throws Exception {
        //given
        SocketChannel socketChannel = mock(SocketChannel.class, RETURNS_DEEP_STUBS);

        willReturn(true).given(socketChannel).isConnected();


        StubSocketChannel channel = new StubSocketChannel();
        SocketDAO socket = new SocketDAOImpl(channel);
        socket.connect();
        //when
        socket.write(message);

        //then
        verify(socketChannel).write(ByteBuffer.wrap(message.getBytes()));
    }


    @Test
    public void shouldReadFromChannel() throws Exception {
        //given

        String expectedResponse = "Something";
        SocketChannel sc = stubReadChannel(expectedResponse);

        SocketDAO socket = new SocketDAOImpl(sc);
        sc.connect(mock(SocketAddress.class));

        //when
        String read = socket.read();

        //then
        assertThat(read).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldThrowConnectionClosedByServerException() throws Exception {
        //given
        SocketChannel channel = endOfStreamReadChannel();

        SocketDAO socket = new SocketDAOImpl(channel);
        channel.connect(mock(SocketAddress.class));

        //when
        when(socket).read();

        //then
        assertThat(caughtException()).isInstanceOf(ConnectionClosedByServerException.class);
    }


    private SocketChannel stubReadChannel(final String shouldReadThat) {
        return new StubSocketChannel() {
            @Override
            public int read(ByteBuffer dst) throws IOException {
                dst.put(shouldReadThat.getBytes());
                return shouldReadThat.length();
            }

        };

    }

    private SocketChannel endOfStreamReadChannel() {
        return new StubSocketChannel() {
            @Override
            public int read(ByteBuffer b) {
                return -1;
            }
        };
    }

}