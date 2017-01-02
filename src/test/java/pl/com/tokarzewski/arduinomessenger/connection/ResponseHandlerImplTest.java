package pl.com.tokarzewski.arduinomessenger.connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;


public class ResponseHandlerImplTest {

    public static final String LF = "\n";
    public static final String END_OF_MESSAGE = ";";
    private SocketDAO socket;
    private ResponseHandler responseHandler;

    @Before
    public void setUp() throws Exception {

        socket = mock(SocketDAO.class);
        responseHandler = new ResponseHandlerImpl(socket);


    }

    @Test
    public void shouldStartReadThread() throws Exception {


        willReturn(true).given(socket).isConnected();
        //when
        responseHandler.startReadThread();

        //then
        assertThat(getRunningThreadsNames()).contains(ResponseHandlerImpl.THREAD_NAME);

    }

    private List<String> getRunningThreadsNames() {
        List<String> threadsNames = new ArrayList<>();
        Set<Thread> threads;
        threads = Thread.getAllStackTraces().keySet();
        for (Thread thread : threads) {
            threadsNames.add(thread.getName());
        }
        return threadsNames;
    }

    @Test
    public void shouldGetResponse() throws Exception {
        //given
        String expectedResponse = "response content" + END_OF_MESSAGE;
        willReturn(true).given(socket).isConnected();
        willReturn(expectedResponse).given(socket).read();
        responseHandler.startReadThread();

        //when
        String response = responseHandler.getResponse();

        //then
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldBeReadyToReadData() throws Exception {
        //given
        willReturn(true).given(socket).isConnected();
        //when
        responseHandler.startReadThread();
        //then
        assertThat(responseHandler.isReadyToRead()).isTrue();
    }

    @Test
    public void shouldNotBeReadyToReadDataWhenSocketIsNotConnected() throws Exception {
        //given
        willReturn(false).given(socket).isConnected();
        //when
        responseHandler.startReadThread();
        //then
        assertThat(responseHandler.isReadyToRead()).isFalse();
    }

    @Test
    public void shouldNotBeReadyToReadDataBeforeStartReadThread() throws Exception {
        //given
        willReturn(true).given(socket).isConnected();


        //then
        assertThat(responseHandler.isReadyToRead()).isFalse();
    }

    @Test
    public void shouldInterruptReadingThread() throws Exception {
        //given
        willReturn(true).given(socket).isConnected();
        responseHandler.startReadThread();
        //when
        responseHandler.interruptReadThread();
        //then
        assertThat(responseHandler.isReadyToRead()).isFalse();
    }

    @Test
    public void shouldHandleDoubledMessage() throws Exception {
        //given
        String firstMessage = "GET" + LF + "get content" + END_OF_MESSAGE;
        String secondMessage = "SEND" + LF + "send content" + END_OF_MESSAGE;
        String doubledMessage = firstMessage + secondMessage;

        StubSocketDAO socket = new StubSocketDAO();
        socket.read.add(doubledMessage);

        ResponseHandler handler = new ResponseHandlerImpl(socket);
        //when
        handler.startReadThread();

        //then
        assertThat(handler.getResponse()).isEqualTo(firstMessage);
        assertThat(handler.getResponse()).isEqualTo(secondMessage);
    }

    @Test
    public void shouldHandleTwoMessagesInParts() throws Exception {
        //given
        String firstMessage = "GET" + LF + "get content" + END_OF_MESSAGE;
        String secondMessagePart1 = "SEND" + LF;
        String secondMessagePart2 = "content" + END_OF_MESSAGE;
        String secondMessageFull = secondMessagePart1 + secondMessagePart2;

        String message = firstMessage + secondMessagePart1;
        StubSocketDAO socket = new StubSocketDAO();
        socket.read.add(message);
        socket.read.add(secondMessagePart2);


        ResponseHandler handler = new ResponseHandlerImpl(socket);
        //when
        handler.startReadThread();

        //then
        assertThat(handler.getResponse()).isEqualTo(firstMessage);

        assertThat(handler.getResponse()).isEqualTo(secondMessageFull);
    }

    @Test
    public void shouldHandleSingleMessage() throws Exception {
        //given
        String message = "GET" + LF + "get content" + END_OF_MESSAGE;

        StubSocketDAO socket = new StubSocketDAO();
        socket.read.add(message);

        ResponseHandler handler = new ResponseHandlerImpl(socket);
        handler.startReadThread();

        assertThat(handler.getResponse()).isEqualTo(message);
    }

    @Test
    public void shouldHandleSingleMessageInParts() throws Exception {
        //given
        String message = "GET" + LF + "get content" + END_OF_MESSAGE;
        StubSocketDAO socket = new StubSocketDAO();
        socket.read.add("GET");
        socket.read.add(LF);
        socket.read.add("get ");
        socket.read.add("content");
        socket.read.add(END_OF_MESSAGE);
        ResponseHandler handler = new ResponseHandlerImpl(socket);
        //when
        handler.startReadThread();

        assertThat(handler.getResponse()).isEqualTo(message);


    }

    @After
    public void tearDown() throws Exception {
        responseHandler.interruptReadThread();
    }
}