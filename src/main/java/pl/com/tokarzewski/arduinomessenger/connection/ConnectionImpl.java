package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.ResponseHandleException;
import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Observer;


public class ConnectionImpl implements Connection {

    private SocketDAO socket;
    private SocketAddress remoteAddress;

    private Sender sender;
    private ResponseHandler responseHandler;

    public ConnectionImpl(String host, int port) {
        this(new SocketDAOImpl(host, port));
    }

    public ConnectionImpl(SocketDAO socket) {
        this.socket = socket;
        this.responseHandler = new ResponseHandlerImpl(this.socket);
        this.sender = new SenderImpl(this.socket);
    }

    @Override
    public void setUpAndConnect() throws IOException {
        connect();
        startReaderThread();
    }

    private void connect() throws IOException {
        this.socket.connect();
    }

    private void startReaderThread() {
        if (isConnected())
            responseHandler.startReadThread();
    }

    @Override
    public void tearDownAndDisconnect() throws IOException {
        this.socket.disconnect();
        responseHandler.interruptReadThread();
    }

    @Override
    public void addResponseObserver(Observer observer) {
        ((ResponseHandlerImpl) responseHandler).addObserver(observer);
    }

    @Override
    public String getResponse() throws ResponseHandleException {
        return this.responseHandler.getResponse();
    }

    @Override
    public void sendMessage(String msg) throws UnableToSendMessageException {
        sender.sendMessage(msg);
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }
}
