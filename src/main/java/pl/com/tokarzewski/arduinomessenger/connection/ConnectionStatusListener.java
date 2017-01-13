package pl.com.tokarzewski.arduinomessenger.connection;

/**
 * Created by Kamil on 2017-01-11.
 */
interface ConnectionStatusListener {
    void connectionStatusChanged(boolean connected);
}
