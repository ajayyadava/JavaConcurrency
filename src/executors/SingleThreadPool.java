package executors;

import java.util.concurrent.*;

public class SingleThreadPool {

    private static final class Task implements Runnable {
        private final String name;

        Task(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            System.out.printf("Task %s has started.\n", name);
            try {
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Task %s has finished.\n", name);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // NOTE: Since this is a single thread executor it doesn't return a threadpoolExecutor instance
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            executor.execute(new Task(String.valueOf(i)));
        }
        TimeUnit.MILLISECONDS.sleep(500);
        executor.shutdown();
    }
}
