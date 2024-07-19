package se.anders_raberg.socketexample;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SocketExampleTest {
    private static final InetSocketAddress DUMMY_SOCK_ADDRESS = new InetSocketAddress("localhost", 12345);
    private Connection _connectionA;
    private Connection _connectionB;

//    @Mock
//    private ScheduledFuture<Void> future;

//    @Mock
//    private ScheduledExecutorService executorService;
//    
//    @Mock
//    private ScheduledFuture<Void> scheduledFuture;
//
//    @Captor
//    private ArgumentCaptor<Runnable> captor;

    @BeforeEach
    void setUp() throws IOException {
//        Mockito.when(scheduledFuture.get()).thenReturn(null);
//        Mockito.when(executorService.scheduleAtFixedRate(Mockito.any(Runnable.class), Mockito.anyLong(),
//                Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenReturn(scheduledFuture);
//        
//        
//        ScheduledFuture<?> scheduleAtFixedRate = executorService.scheduleAtFixedRate(() -> System.out.println("HELLO"), 0, 1, TimeUnit.SECONDS);
//        
//        System.out.println(String.valueOf(scheduleAtFixedRate));
//
//        Mockito.verify(executorService).scheduleAtFixedRate(captor.capture(), Mockito.anyLong(), Mockito.anyLong(),
//                Mockito.any(TimeUnit.class));
//        
//        captor.getValue().run();
//        captor.getValue().run();
//        
        
        ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);

        // Create a completed future with a result of "done"
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);

        // Create a completed ScheduledFuture with a result of "done"
        ScheduledFuture<Void> scheduledFuture = mock(ScheduledFuture.class);
//        when(scheduledFuture.get()).thenReturn(null);

        // When scheduleAtFixedRate() is called on the executorService with any arguments, return the completed ScheduledFuture
//        when(executorService.scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(scheduledFuture);

        // Use the executorService in your test code
//        ScheduledFuture<String> result = executorService.scheduleAtFixedRate(() -> System.out.println("Hello, world!"), 0L, 1L, TimeUnit.SECONDS);

        // Verify that the result is "done"
//        assertEquals("done", result.get());

        BlockingQueue<DatagramPacket> aToB = new LinkedBlockingQueue<>();
        BlockingQueue<DatagramPacket> bToA = new LinkedBlockingQueue<>();
        SimulatedSocket socketA = new SimulatedSocket(bToA, aToB);
        SimulatedSocket socketB = new SimulatedSocket(aToB, bToA);
        _connectionA = new Connection(socketA);
        _connectionB = new Connection(socketB);
    }

    @Test
    void test() throws IOException {
        final byte[] buf1 = { 10, 20, 30 };
        DatagramPacket out1 = new DatagramPacket(buf1, buf1.length, DUMMY_SOCK_ADDRESS);
        _connectionA.send(out1);
        DatagramPacket in1 = _connectionB.receive();
        Assertions.assertArrayEquals(out1.getData(), in1.getData());

        final byte[] buf2 = { 11, 22, 33 };
        DatagramPacket out2 = new DatagramPacket(buf2, buf2.length, DUMMY_SOCK_ADDRESS);
        _connectionB.send(out2);
        DatagramPacket in2 = _connectionA.receive();
        Assertions.assertArrayEquals(out2.getData(), in2.getData());
    }
}
