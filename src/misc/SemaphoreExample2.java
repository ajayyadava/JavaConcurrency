package misc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**

 Semaphores can control either access to a pool of resources or act as a mutex for a critical section.

 Binary semaphores act as a mutex for a critical section.

 When you initialize a semaphore, it is just the initial value and not the capacity.
 So semaphores can take negative value as well.

 This example demonstrates how you can use Semaphore with a negative value like a countdown latch.

 */
public class SemaphoreExample2 {

    public static void main(String[] args) {
        Semaphore s = new Semaphore(-1);

        Runnable task1 = () -> {
            System.out.println("Task1 started");
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task1 finished");
            s.release();
        };

        Runnable task2 = () -> {
            try {
                s.acquire();
                System.out.println("Task2 started");
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println("Task2 finished");
                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // create 2 of type task1 and one of type task2
        // only after both task1 have finished, will task2 be executed, even though it is started first.
        new Thread(task2).start();
        new Thread(task1).start();
        new Thread(task1).start();

    }


}
