/*
 * The MIT License
 *
 * Copyright 2016 Kamil Tokarzewski.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.com.tokarzewski.arduinomessenger.messages;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloMessageTest {

    private HelloMessage hello = new HelloMessage("Kamil");

    @Test
    public void shouldMatchGivenJsonString() throws Exception {

        String expectedFormat = "HELLO\n" +
                "{\"userAgent\":{\"version\":\"0.1.2/Mini\",\"os\":\"Windows 7/6.1\"},\"id\":\"Kamil\"};";

        assertThat(hello.toString()).isEqualTo(expectedFormat);

    }

    @Test
    public void toStringReturnsTypeInFirstLine(){
        //given
        String expected = MessageTypes.HELLO;

        //when
        Iterable<String> split = Splitter.on("\n").split(hello.toString());
        String result = split.iterator().next();

        //then
        assertThat(result).isEqualTo(expected);
        
    }
    
    @Test
    public void shouldCheckIfSecondLineIsContent(){
        // given
        String expectedJsonString = getJsonString(hello);

        // when
        List<String> split = Splitter.on("\n")
                .trimResults(CharMatcher.anyOf(" ;"))
                .splitToList(hello.toString());
        String splitResult = split.get(1).trim();


        //then
        assertThat(splitResult)
                .isEqualTo(expectedJsonString);
    }

    private String getJsonString(HelloMessage hello) {
        Gson gson = new GsonBuilder().setLenient().create();
        return gson.toJson(hello);
    }

}
