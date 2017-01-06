package misc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderExample2 {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<Integer, LongAdder> freqs = new ConcurrentHashMap<>();

        // Notice the Closure
        Runnable t = () -> {
            for (int i = 0; i < 10; i++) {
                freqs.computeIfAbsent(i, k -> new LongAdder()).add(1);
            }
        };

        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(t);
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
        for (Map.Entry<Integer, LongAdder> entry : freqs.entrySet()) {
            System.out.printf("%d : %d, ", entry.getKey(), entry.getValue().sum());
        }
    }
}
