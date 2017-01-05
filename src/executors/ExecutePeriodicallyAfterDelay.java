package executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ExecutePeriodicallyAfterDelay {
    private static final class Task implements Runnable {

        private final String name;

        Task(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            System.out.printf("Task %s has started.\n", name);
            try {
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Task %s has finished.\n", name);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // NOTE: No need to cast to ScheduledThreadPool since we don't need pool stats,
        // ScheduledExecutorService is enough to schedule
        ScheduledExecutorService executor =  Executors.newScheduledThreadPool(2);

        // NOTE:
        // Tasks need to satisfy only one conditions before they start
        // - delay(initialDelay for first task) amount of time has passed before the previous execution
        // because of this there will never be 2 tasks running in parallel in this case also
        executor.scheduleWithFixedDelay(new Task("t1"), 100, 100, TimeUnit.MILLISECONDS);

        // since tasks take 200 ms to complete - only 2 tasks will be scheduled
        // 1st task at 100, second at 100 + 200 + 100=400
        // 3rd at 400 + 200 + 100 = 700
        TimeUnit.MILLISECONDS.sleep(500);
        executor.shutdown();

    }
}
