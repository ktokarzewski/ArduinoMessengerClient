package pl.com.tokarzewski.arduinomessenger;


import org.junit.Test;
import pl.com.tokarzewski.arduinomessenger.json.JsonParser;
import pl.com.tokarzewski.arduinomessenger.utils.ArduUserAgent;

import static org.assertj.core.api.Assertions.assertThat;

public class ArduUserAgentTest {
    @Test
    public void shouldDeserializeAgentFromString() throws Exception {
        //given
        String os = "osName/osVersion";
        String version = "0.0.0/codename";
        String jsonString = "{os:\"" + os + "\",version : \"" + version + "\"}";
        ArduUserAgent expectedAgent = new ArduUserAgent(os, version);

        //when
        ArduUserAgent result = JsonParser
                .getParser()
                .fromJson(jsonString, ArduUserAgent.class);

        //then
        assertThat(result).isEqualTo(expectedAgent);
    }


}