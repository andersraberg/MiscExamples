package se.anders_raberg.queue_test;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueTest {
    private static final BlockingQueue<Integer> QA = new LinkedBlockingQueue<>(1);
    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws InterruptedException {

        EXEC.submit(() -> {
            int counter = 0;
            while (true) {
                try {
                    System.out.println(Thread.currentThread().getName() + " > " + counter++);
                    boolean offer1 = QA.offer(1, 1000, TimeUnit.SECONDS);
                    System.out.println(Thread.currentThread().getName() + " > " + offer1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread.sleep(100);
        System.out.println(Thread.currentThread().getName() + " > " + "A: " + QA);
        QA.retainAll(List.of(1, 3, 4));
        System.out.println(Thread.currentThread().getName() + " > " + "B: " + QA);

        Thread.sleep(3000);
        QA.retainAll(Collections.emptyList());
        Thread.sleep(3000);
        System.exit(0);
    }

}
