package pl.com.tokarzewski.arduinomessenger.connection;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;


public class ResponseHandlerImplTest {

    private static final String LF = "\n";
    private static final String END_OF_MESSAGE = ";";


    @Test
    public void shouldStartReadThread() throws Exception {

        SocketDAO socket = mock(SocketDAO.class);
        ResponseHandlerImpl responseHandler = new ResponseHandlerImpl(socket);


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
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();
        willReturn(expectedResponse).given(socket).read();

        ResponseHandlerImpl responseHandler = new ResponseHandlerImpl(socket);
        SynchronousObserver observer = new SynchronousObserver();
        responseHandler.addObserver(observer);

        //when
        responseHandler.startReadThread();
        String response = observer.getData();

        //then
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldBeReadyToReadData() throws Exception {

        //given
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();

        ResponseHandlerImpl responseHandler = new ResponseHandlerImpl(socket);

        //when
        responseHandler.startReadThread();

        //then
        assertThat(responseHandler.isReadyToRead()).isTrue();
    }

    @Test
    public void shouldNotBeReadyToReadDataWhenSocketIsNotConnected() throws Exception {

        //given
        SocketDAO socket = mock(SocketDAO.class);
        ResponseHandlerImpl responseHandler = new ResponseHandlerImpl(socket);
        willReturn(false).given(socket).isConnected();

        //when
        responseHandler.startReadThread();
        //then
        assertThat(responseHandler.isReadyToRead()).isFalse();
    }

    @Test
    public void shouldNotBeReadyToReadDataBeforeStartReadThread() throws Exception {

        //given
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();

        //when
        ResponseHandlerImpl responseHandler = new ResponseHandlerImpl(socket);

        //then
        assertThat(responseHandler.isReadyToRead()).isFalse();
    }

    @Test
    public void shouldInterruptReadingThread() throws Exception {
        //given
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();
        ResponseHandlerImpl responseHandler = new ResponseHandlerImpl(socket);
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
        String secondMessage = "PUT" + LF + "send content" + END_OF_MESSAGE;
        String doubledMessage = firstMessage + secondMessage;

        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();
        willReturn(doubledMessage).given(socket).read();

        ObservableResponseHandler handler = new ResponseHandlerImpl(socket);
        SynchronousObserver observer = new SynchronousObserver();
        handler.addObserver(observer);

        //when
        handler.startReadThread();

        //then
        assertThat(observer.getData()).isEqualTo(firstMessage);
        assertThat(observer.getData()).isEqualTo(secondMessage);

        handler.interruptReadThread();

    }

    @Test
    public void shouldHandleTwoMessagesInParts() throws Exception {
        //given
        String firstMessage = "GET" + LF + "get content" + END_OF_MESSAGE;
        String secondMessagePart1 = "PUT" + LF;
        String secondMessagePart2 = "content" + END_OF_MESSAGE;
        String secondMessageFull = secondMessagePart1 + secondMessagePart2;

        String message = firstMessage + secondMessagePart1;
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();
        willReturn(message)
                .willReturn(secondMessagePart2)
                .given(socket).read();

        ObservableResponseHandler handler = new ResponseHandlerImpl(socket);
        SynchronousObserver observer = new SynchronousObserver();
        handler.addObserver(observer);

        //when
        handler.startReadThread();

        //then
        assertThat(observer.getData()).isEqualTo(firstMessage);
        assertThat(observer.getData()).isEqualTo(secondMessageFull);
        handler.interruptReadThread();

    }

    @Test
    public void shouldHandleSingleMessage() throws Exception {
        //given
        String message = "GET" + LF + "get content" + END_OF_MESSAGE;

        SocketDAO socket = mock(SocketDAO.class);
        willReturn(message).given(socket).read();
        willReturn(true).given(socket).isConnected();

        ObservableResponseHandler handler = new ResponseHandlerImpl(socket);

        SynchronousObserver observer = new SynchronousObserver();
        handler.addObserver(observer);
        handler.startReadThread();

        assertThat(observer.getData()).isEqualTo(message);

        handler.interruptReadThread();

    }

    @Test
    public void shouldHandleSingleMessageInParts() throws Exception {
        //given
        String message = "GET" + LF + "get content" + END_OF_MESSAGE;
        SocketDAO socket = mock(SocketDAO.class);
        willReturn(true).given(socket).isConnected();
        willReturn("GET")
                .willReturn(LF)
                .willReturn("get ")
                .willReturn("content")
                .willReturn(END_OF_MESSAGE)
                .given(socket).read();

        ObservableResponseHandler handler = new ResponseHandlerImpl(socket);
        SynchronousObserver observer = new SynchronousObserver();
        handler.addObserver(observer);

        //when
        handler.startReadThread();

        assertThat(observer.getData()).isEqualTo(message);

        handler.interruptReadThread();

    }

}