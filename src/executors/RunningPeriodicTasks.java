package executors;

import java.util.concurrent.*;

public class RunningPeriodicTasks {

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

        // NOTE:
        // Tasks need to satisfy 2 conditions before they start
        // - their trigger time is met i.e. for nth task = initialDelay + n * period time has passed
        // - No other previous task is getting executed

        // Note: the second task will not wait till "period" amount of time after first task finishes,
        // it will create second task at exactly initialDelay + 2 * period but if the first task hasn't finished till
        // then, then it will wait for the task to finish and will start immediately after that.
        executor.scheduleAtFixedRate(new Task("t1"), 100, 100, TimeUnit.MILLISECONDS);

        // 5 tasks will be triggered in 500 ms sleep, all will be executed but none will be parallel
        TimeUnit.MILLISECONDS.sleep(500);
        executor.shutdown();

    }
}
