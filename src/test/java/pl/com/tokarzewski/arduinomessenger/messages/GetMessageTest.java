package pl.com.tokarzewski.arduinomessenger.messages;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GetMessageTest {

    @Test
    public void toStringShouldMatchGivenString() throws Exception {

        //given
        String expectedFormat = "GET\n" +
                "{\"request\":\"banana\",\"id\":\"Kamil\"};";

        GetMessage message = new GetMessage("Kamil", "banana");

        assertThat(message.toString()).isEqualTo(expectedFormat);

    }

    @Test
    public void shouldSerializeToGetMessage() throws Exception {
        Message get = new GetMessage("Kamil", "banana");
        String expectedFormat = "GET\n" +
                "{\"request\":\"banana\",\"id\":\"Kamil\"};";
        System.out.println(get.toString());
        assertThat(get.toString()).isEqualTo(expectedFormat);

    }
}