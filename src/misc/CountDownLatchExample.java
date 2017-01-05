package misc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**

 Properties of CountdownLatch
 ==============================

 1. CountdownLatches don't have concept of one countdown per thread, they just have a concept of countdown.
 Even a single thread can count down twice and it will be counted twice.


 */
public class CountDownLatchExample {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(2);

        Runnable task1 = () -> {
            System.out.println("Task1 has started.");
            // countdown twice from the same thread
            latch.countDown();
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println("Task1 has finished.");
        };

        Runnable task2 = () -> {
            try {
                System.out.println("Task2 waiting to start.");
                // wait till 2 countdowns
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task2 has started.");
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task2 has finished.");
        };

        new Thread(task2).start();
        new Thread(task1).start();
    }
}
