/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.tokarzewski.arduinomessenger.messages;


import pl.com.tokarzewski.arduinomessenger.exceptions.MessageDeserializerException;

public interface FrameDeserializer {
    ProtocolFrame deserializeMessage(String message) throws MessageDeserializerException;

}
