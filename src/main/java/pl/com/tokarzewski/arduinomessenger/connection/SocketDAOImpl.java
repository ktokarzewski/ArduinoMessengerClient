package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.ConnectionClosedByServerException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;


public class SocketDAOImpl implements SocketDAO {

    private static final int BUFFER_CAPACITY = 512;
    private static final int END_OF_STREAM = -1;
    private SocketChannel channel;
    private ByteBuffer writeBuffer;
    private ByteBuffer readBuffer;
    private static SocketAddress remoteAddress;
    private int bytesRead;
    private String responseString;

    protected SocketDAOImpl() {
    }

    public SocketDAOImpl(String host, int port) {
        this(new InetSocketAddress(host, port));
    }

    public SocketDAOImpl(SocketAddress address) {
        remoteAddress = address;
        readBuffer = ByteBuffer.allocate(BUFFER_CAPACITY);
        bytesRead = 0;
    }

    public SocketDAOImpl(SocketChannel channel) throws IOException {
        this(channel.getRemoteAddress());
        this.channel = channel;
    }

    public void setReadBuffer(ByteBuffer readBuffer) {
        this.readBuffer = readBuffer;
    }

    @Override
    public void write(String message) throws IOException {
        if (isConnected()) {
            putMessageInWriteBuffer(message);
            channel.write(writeBuffer);
        } else {
            throw new NotYetConnectedException();
        }
    }

    @Override
    public boolean isConnected() {
        return channel != null && channel.isConnected();
    }

    private void putMessageInWriteBuffer(String message) {
        writeBuffer = ByteBuffer.wrap(message.getBytes());
    }

    @Override
    public String read() throws IOException {
        if (isConnected()) {
            while (incomingDataAvailable()) {
                bytesRead = channel.read(readBuffer);
                if (reachedEndOfStream()) {
                    disconnect();
                    throw new ConnectionClosedByServerException("Reached end of stream");
                }

            }
            extractResponse();
            return responseString;
        } else {
            throw new NotYetConnectedException();
        }
    }

    private boolean incomingDataAvailable() {
        return bytesRead <= 0;
    }

    private boolean reachedEndOfStream() {
        return bytesRead == END_OF_STREAM;
    }

    @Override
    public boolean disconnect() throws IOException {
        channel.close();
        return isConnected();
    }

    private void extractResponse() {
        responseString = new String(Arrays.copyOf(readBuffer.array(), bytesRead));
        bytesRead = 0;
        readBuffer.clear();
    }

    @Override
    public boolean connect() throws IOException {
        channel = SocketChannel.open();
        channel.connect(remoteAddress);

        return isConnected();
    }
}
