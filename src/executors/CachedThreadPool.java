package executors;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**

 CachedThreadPool will keep launching new threads for tasks whenever required, so are not suitable for most production
 applications until max. size can be determined to be very small.


 */
public class CachedThreadPool {

    private static final class Task implements Runnable {
        private final String name;

        Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.printf("Task %s has started\n", name);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // You have to cast it to ThreadPoolExecutor to get stats like poolsize
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executor.execute(new Task(String.valueOf(i)));
            System.out.println("Thread pool size is: " + executor.getPoolSize());
        }
        executor.shutdown();
    }

}
