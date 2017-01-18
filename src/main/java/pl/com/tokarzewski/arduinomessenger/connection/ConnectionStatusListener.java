package pl.com.tokarzewski.arduinomessenger.connection;


public interface ConnectionStatusListener {
    void onConnectionStatusChange(boolean connected);
}
