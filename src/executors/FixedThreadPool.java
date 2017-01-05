package executors;

import java.util.concurrent.*;

public class FixedThreadPool {
    private static final class Task implements Runnable {

        private final String name;

        Task(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            System.out.printf("Task %s has started.\n", name);
            try {
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Task %s has finished.\n", name);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        // NOTE: you have to cast to threadpoolexecutor to get stats
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executor.execute(new Task(String.valueOf(i)));
        }
        for (int i = 0; i < 10; i++) {
            System.out.printf("Current Pool Size: %d, Completed Tasks: %d, Active Threads: %d\n",
                    executor.getPoolSize(), executor.getCompletedTaskCount(), executor.getActiveCount());
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100));

        }
        System.out.println("Maximum size: " + executor.getMaximumPoolSize());
    }
}
