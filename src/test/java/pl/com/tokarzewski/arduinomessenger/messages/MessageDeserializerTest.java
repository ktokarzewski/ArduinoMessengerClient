package pl.com.tokarzewski.arduinomessenger.messages;

import com.google.gson.JsonSyntaxException;
import org.junit.Before;
import org.junit.Test;
import pl.com.tokarzewski.arduinomessenger.utils.ArduUserAgent;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;


public class MessageDeserializerTest {
    private FrameDeserializer resolver;
    private String helloString = new HelloMessage("identity").toString();

    @Before
    public void beforeTest() {
        resolver = new MessageDeserializer();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenIncorrectDataPassed() {

        when(resolver).deserializeMessage("MALFORMED DATA");

        assertThat(caughtException())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenEmptyStringPassed() {
        when(resolver).deserializeMessage("");

        assertThat(caughtException())
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void shouldThrowJSonSyntaxException() throws JsonSyntaxException {
        when(resolver).deserializeMessage("GET\n ILLEGAL CHARACTERS");

        assertThat(caughtException())
                .isInstanceOf(JsonSyntaxException.class);
    }

    @Test
    public void testHelloFrameDeserialization() {
        HelloMessage hello = (HelloMessage) resolver.deserializeMessage(helloString);
        ArduUserAgent userAgent = ArduUserAgent.getDefaultUserAgent();
        String expected = userAgent.toString();
        String actual = hello.getUserAgent();
        assertThat(actual).isEqualTo(expected);

    }


    @Test
    public void shouldGetHelloType() {
        //given
        String expected = MessageTypes.HELLO;

        //when
        ProtocolFrame f = resolver.deserializeMessage(helloString);

        //then
        assertThat(f.getType()).isEqualTo(expected);
    }

    @Test
    public void shouldDeserializeAsGetMessage() throws Exception {

        String getMessage = "GET\n" +
                "{\"request\":\"banana\",\"content\":{},\"id\":\"Kamil\"};";

        assertThat(resolver.deserializeMessage(getMessage)).isInstanceOf(GetMessage.class);

    }

    @Test
    public void shouldDeserializeAsGetMessageWithoutContent() throws Exception {

        String getMessage = "GET\n" +
                "{\"request\":\"banana\",\"id\":\"Kamil\"};";

        assertThat(resolver.deserializeMessage(getMessage)).isInstanceOf(GetMessage.class);

    }

    @Test
    public void shouldDeserializeAsSendMessage() throws Exception {

        String sendMessage = "SEND\n" +
                "{\"resource\":\"12345\",\"value\":\"100 200 300 400 1000 200 300 400\",\"id\":\"Kamil\"};";

        assertThat(resolver.deserializeMessage(sendMessage)).isInstanceOf(SendMessage.class);

    }



    @Test
    public void shouldThrowIllegalArgumentExceptionWhenGetDoubledMessageContent() throws Exception {

        String getMessage = "GET\n" +
                "{\"request\":\"banana\",\"content\":{},\"id\":\"Kamil\"}\r\n"
                + "GET\n" +
                "{\"request\":\"banana\",\"content\":{},\"id\":\"Kamil\"}\r\n";

        when(resolver).deserializeMessage(getMessage);

        assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class);

    }


}