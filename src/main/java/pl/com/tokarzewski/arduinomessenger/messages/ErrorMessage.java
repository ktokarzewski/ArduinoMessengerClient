/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.tokarzewski.arduinomessenger.messages;


public class ErrorMessage extends Message {
    private static final String TYPE = MessageTypes.ERROR;
    private String message;


    public ErrorMessage(String id, String message) {
        super(id);
        this.message = message;
    }

    public ErrorMessage(String message) {
        this(defaultId, message);
    }


    public String getMessage() {
        return this.message;
    }

    @Override
    public String getType() {
        return ErrorMessage.TYPE;
    }

}
