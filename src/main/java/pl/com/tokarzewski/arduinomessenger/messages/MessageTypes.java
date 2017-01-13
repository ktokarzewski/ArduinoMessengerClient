/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.tokarzewski.arduinomessenger.messages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Toki
 */
public class MessageTypes {
    public static final String HELLO = "HELLO";
    public static final String PUT = "PUT";
    public static final String GET = "GET";
    public static final String ERROR = "ERROR";
    private static final Map<String,Class> CLASS_MAP = initializeMap();
    
    private static Map<String,Class> initializeMap(){
        Map<String,Class> map = new HashMap<>();
        map.put(HELLO, HelloMessage.class);
        map.put(PUT, PutMessage.class);
        map.put(GET, GetMessage.class);
        return map;
    }
    
    public static Class getMessageClass(String type){
        return CLASS_MAP.get(type);
    }
    
    public static List<String> getValidTypeList(){
        return Arrays.asList(HELLO, PUT, GET);
    }
}
