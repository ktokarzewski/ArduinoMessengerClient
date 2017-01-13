package pl.com.tokarzewski.arduinomessenger.connection;

/**
 * Created by Kamil on 2017-01-12.
 */
public interface IncomingDataListener {
    void onNewIncomingData(String data);
}
