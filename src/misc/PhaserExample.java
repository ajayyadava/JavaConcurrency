package misc;

import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**

 Run 10 threads - in 3 phases
 First phase - generate random number < 10
 After firsts phase, threads whose number < 5 will deregister while others will follow to be synced
 Second phase multiply that number by 2
 Third phase multiply that number by 2 again.

 Note: that the threads which call deregister once, don't wait for other threads even if you called
 arriveAndAwaitAdvance()

 Note:
 1. It is an error to call wait or deregister once you have deregistered

 2. There is no bookkeeping in the phaser, so you can't check if current thread is registered. However, it is
 possible to inherit the Phaser class and implement that bookkeeping()

 3. You can create a phaser with x participants or call .register() for each thread.

 4. You can dynamically add or dynamically remove the number of parties in the phaser.
 For adding there are 2 options: - call register(), call bulkregister(int)

 5. register() - just increments the number of parties by 1, It doesn't register the current thread or any given thread
 because phaser doesn't have any bookkeeping. It just has a count.

 6. Similarly arriveAndDeregister() can also be called from anywhere. It just decreases the count of calls to be waited
 However, it suspends the thread calling this method till all have reached that phase, so you can't call twice
 for a single phase, just like CyclicBarrier.

 */
public class PhaserExample {

    private static final class Task implements Runnable {
        private final String name;
        private final Phaser phaser;

        Task(String name, Phaser phaser) {
            this.name = name;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            // phase 1
            int number = ThreadLocalRandom.current().nextInt(10);
            System.out.println(name + ", " + number);
            if (number < 5) {
                phaser.arriveAndDeregister();
            } else {
                // give chance which are less a chance to complete all phases
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                phaser.arriveAndAwaitAdvance();

                // phase 2
                number *= 2;
                System.out.println(name + ", Phase2 completed" );
                phaser.arriveAndAwaitAdvance();

                // phase3
                number *= 2;
                System.out.println(name + ", Phase3 completed");
                phaser.arriveAndAwaitAdvance();
            }
            System.out.println(name + " completed.");
        }
    }

    public static void main(String[] args) {
        Phaser phaser = new Phaser();
        for (int i = 0; i < 5; i++) {
            phaser.register();
            new Thread(new Task("task" + i, phaser)).start();
        }

    }
}
