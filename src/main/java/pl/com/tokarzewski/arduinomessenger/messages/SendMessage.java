/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.tokarzewski.arduinomessenger.messages;

public class SendMessage extends Message {

    private static final String TYPE = MessageTypes.SEND;
    private String resource;
    private String value;

    public SendMessage(String id, String resource, String value) {
        super(id);
        this.resource = resource;
        this.value = value;
    }

    public SendMessage(String resource, String value) {
        this(defaultId, resource, value);
    }


    public String getValue() {
        return this.value;
    }

    public String getResource() {
        return this.resource;
    }

    @Override
    public String getType() {
        return SendMessage.TYPE;
    }


}
