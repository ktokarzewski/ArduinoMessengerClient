/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.tokarzewski.arduinomessenger.messages;

import pl.com.tokarzewski.arduinomessenger.utils.ArduUserAgent;

public class HelloMessage extends Message {
    private static final String TYPE = MessageTypes.HELLO;
    private ArduUserAgent userAgent;


    public HelloMessage(String id) {
        super(id);
        this.userAgent = ArduUserAgent.getDefaultUserAgent();
    }

    public HelloMessage() {
        this(defaultId);
    }


    public String getUserAgent() {
        return userAgent.toString();
    }

    @Override
    public String getType() {
        return HelloMessage.TYPE;
    }

}
