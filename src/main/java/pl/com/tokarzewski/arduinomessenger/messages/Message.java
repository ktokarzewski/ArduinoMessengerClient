/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.tokarzewski.arduinomessenger.messages;

import pl.com.tokarzewski.arduinomessenger.json.JsonObject;


public abstract class Message extends JsonObject implements ProtocolFrame {
    static String defaultId = System.getProperty("user.name", "anonymous");
    protected String id;

    Message(String id) {
        this.id = id;
    }

    public static void setDefaultId(String id) {
        Message.defaultId = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return getType() + "\n" + super.toString() + ";";
    }

}
