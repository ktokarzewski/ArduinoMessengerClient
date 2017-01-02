/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.tokarzewski.arduinomessenger.messages;


public class GetMessage extends Message {
    private static final String TYPE = MessageTypes.GET;
    private String request;


    public GetMessage(String id, String request) {
        super(id);
        this.request = request;
    }

    public GetMessage(String request) {
        this(defaultId, request);
    }


    public String getRequest() {
        return this.request;
    }


    @Override
    public String getType() {
        return GetMessage.TYPE;
    }


}
