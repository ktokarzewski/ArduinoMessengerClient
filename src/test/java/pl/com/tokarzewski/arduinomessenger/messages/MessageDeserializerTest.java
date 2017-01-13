package pl.com.tokarzewski.arduinomessenger.messages;

import com.google.gson.JsonSyntaxException;
import org.junit.Before;
import org.junit.Test;
import pl.com.tokarzewski.arduinomessenger.exceptions.InvalidMessageFormatException;
import pl.com.tokarzewski.arduinomessenger.exceptions.MessageDeserializerException;
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
    public void shouldThrowIllegalArgumentExceptionWhenIncorrectDataPassed() throws MessageDeserializerException {

        when(resolver).deserializeMessage("MALFORMED DATA");

        assertThat(caughtException())
                .isInstanceOf(InvalidMessageFormatException.class);
    }

    @Test
    public void shouldThrowInvalidMessageFormatWhenEmptyStringPassed() throws MessageDeserializerException {
        when(resolver).deserializeMessage("");

        assertThat(caughtException())
                .isInstanceOf(InvalidMessageFormatException.class);
    }


    @Test
    public void shouldThrowJSonSyntaxException() throws MessageDeserializerException {
        when(resolver).deserializeMessage("GET\n ILLEGAL CHARACTERS");

        assertThat(caughtException())
                .isInstanceOf(JsonSyntaxException.class);
    }

    @Test
    public void testHelloFrameDeserialization() throws MessageDeserializerException {
        HelloMessage hello = (HelloMessage) resolver.deserializeMessage(helloString);
        ArduUserAgent userAgent = ArduUserAgent.getDefaultUserAgent();
        String expected = userAgent.toString();
        String actual = hello.getUserAgent();
        assertThat(actual).isEqualTo(expected);

    }


    @Test
    public void shouldGetHelloType() throws MessageDeserializerException {
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
    public void shouldDeserializeAsPutMessage() throws Exception {

        String sendMessage = "PUT\n" +
                "{\"resource\":\"12345\",\"value\":\"100 200 300 400 1000 200 300 400\",\"id\":\"Kamil\"};";

        assertThat(resolver.deserializeMessage(sendMessage)).isInstanceOf(PutMessage.class);

    }



    @Test
    public void shouldThrowInvalidMessageFormatExceptionWhenMessageContentIsDoubled() throws Exception {

        String getMessage = "GET\n" +
                "{\"request\":\"banana\",\"content\":{},\"id\":\"Kamil\"};"
                + "GET\n" +
                "{\"request\":\"banana\",\"content\":{},\"id\":\"Kamil\"};";

        when(resolver).deserializeMessage(getMessage);

        assertThat(caughtException()).isInstanceOf(InvalidMessageFormatException.class);

    }


}