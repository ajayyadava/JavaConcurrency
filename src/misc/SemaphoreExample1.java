package misc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**


 Example Usage of Semaphore:

 1. Unlike locks, semaphores don't have concept of thread ownership. This makes them good candidates to break deadlocks

 This example illustrates point 1.
Problem Statement: Case 1 - Run 3 tasks in order.
Source: CTCI - 6th Edition
 */

public class SemaphoreExample1 {



    public static void main(String[] args) throws InterruptedException {
        Semaphore lock1 = new Semaphore(1);
        Semaphore lock2 = new Semaphore(1);
        Semaphore lock3 = new Semaphore(1);
        lock2.acquire();
        lock3.acquire();

        Runnable task1 = () -> {
            System.out.println("Task1 running");
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task1 finished");
            lock1.release();
            lock2.release();
        };


        Runnable task2 = () -> {
            try {
                lock2.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task2 running");
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task2 finished");
            lock2.release();
            lock3.release();
        };


        Runnable task3 = () -> {
            try {
                lock3.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task3 running");
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task3 finished");
            lock3.release();
        };

        new Thread(task1).start();
        new Thread(task2).start();
        new Thread(task3).start();
    }
}
