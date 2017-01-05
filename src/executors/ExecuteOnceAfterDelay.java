package executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ExecuteOnceAfterDelay {

    private static final class Task implements Runnable {

        private final String name;

        Task(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            System.out.printf("Task %s has started.\n", name);
            try {
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(150));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Task %s has finished.\n", name);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // NOTE: ScheduledExecutorService is enough to schedule since we don't need pool stats
        ScheduledExecutorService executor =  Executors.newScheduledThreadPool(2);

        // Execute only once - after a delay of 100 Miliseconds
        executor.schedule(new Task("t2"), 100, TimeUnit.MILLISECONDS);

    }
}
