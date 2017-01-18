package pl.com.tokarzewski.arduinomessenger.connection;

import pl.com.tokarzewski.arduinomessenger.exceptions.ConnectionClosedByServerException;
import pl.com.tokarzewski.arduinomessenger.exceptions.UnableToSendMessageException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class ConnectionImpl implements Connection, Observer {

    private SocketDAO socket;
    private Sender sender;
    private ObservableResponseHandler responseHandler;
    private List<ConnectionStatusListener> connectionStatusListeners;
    private List<IncomingDataListener> incomingDataListeners;

    public ConnectionImpl(String host, int port) {
        this(new SocketDAOImpl(host, port));
    }

    public ConnectionImpl(SocketDAO socket) {
        this(socket, new ResponseHandlerImpl(socket), new SenderImpl(socket));
    }

    public ConnectionImpl(SocketDAO socket, ObservableResponseHandler handler, Sender sender) {
        this.socket = socket;
        this.responseHandler = handler;
        this.sender = sender;
        this.responseHandler.addObserver(this);

        this.connectionStatusListeners = new ArrayList<>();
        this.incomingDataListeners = new ArrayList<>();
    }

    public void setResponseHandler(ObservableResponseHandler responseHandler) {
        this.responseHandler.deleteObservers();
        this.responseHandler = responseHandler;
        this.responseHandler.addObserver(this);
    }



    @Override
    public void setupAndConnect() throws IOException {
        connect();
        startReaderThread();
        notifyConnectionStatusListeners();
    }

    private void connect() throws IOException {
        this.socket.connect();
    }

    private void startReaderThread() {
        responseHandler.startReadThread();
    }

    @Override
    public void tearDownAndDisconnect() throws IOException {
        try {
            this.socket.disconnect();
        } catch (IOException e) {
            throw e;
        } finally {
            notifyConnectionStatusListeners();
            responseHandler.interruptReadThread();
        }
    }




    @Override
    public void sendMessage(String msg) throws UnableToSendMessageException {
        sender.sendMessage(msg);
    }

    @Override
    public void addIncomingDataListener(IncomingDataListener listener) {
        incomingDataListeners.add(listener);
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ObservableResponseHandler) {
            if (arg instanceof ConnectionClosedByServerException) {
                notifyConnectionStatusListeners();
            } else if (arg instanceof String) {
                notifyIncomingDataListeners((String) arg);
            }
        }
    }

    private void notifyIncomingDataListeners(String data) {
        for (IncomingDataListener listener : incomingDataListeners) {
            listener.onNewIncomingData(data);
        }
    }

    private void notifyConnectionStatusListeners() {
        for (ConnectionStatusListener listener : connectionStatusListeners) {
            listener.onConnectionStatusChange(isConnected());
        }
    }

    public void addConnectionStatusListener(ConnectionStatusListener listener) {
        connectionStatusListeners.add(listener);
    }
}
