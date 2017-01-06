package misc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAccumulator;

public class LongAccumulatorExample {

    public static void main(String[] args) throws InterruptedException {

        // Maintain a maximum among all the values entered for each key
        ConcurrentHashMap<Integer, LongAccumulator> freqs = new ConcurrentHashMap<>();

        // Notice the Closure - how runnable is accessing the freqs
        Runnable t = () -> {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    freqs.computeIfAbsent(i, k -> new LongAccumulator(Long::max, 0L)).accumulate(j);
                }
            }
        };

        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(t);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (Map.Entry<Integer, LongAccumulator> entry : freqs.entrySet()) {
            System.out.printf("%d : %d, ", entry.getKey(), entry.getValue().longValue());
        }
    }
}
