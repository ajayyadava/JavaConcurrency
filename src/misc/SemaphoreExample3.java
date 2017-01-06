package misc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**

 Semaphores as access to a pool of resources.

 */
public class SemaphoreExample3 {

    private static final class Task implements Runnable {

        private final String name;
        private final Semaphore semaphore;

        Task(String name, Semaphore semaphore) {
            this.name = name;
            this.semaphore = semaphore;
        }
        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.printf("%s started\n", name);
                TimeUnit.MILLISECONDS.sleep(300);
                System.out.printf("%s finished\n", name);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Semaphore s = new Semaphore(3);
        for (int i = 0; i < 6; i++) {
            new Thread(new Task("Task"+i, s)).start();
        }
    }
}