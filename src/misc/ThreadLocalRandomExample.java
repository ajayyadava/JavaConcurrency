package misc;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**

 Use ThreadLocalRandom Instead of Random for generating random numbers


 Notes:

 IntStream.range(0,10)
 IntStream.rangeClosed(0,10)

 ## Expression lambda
 .forEach(i -> new Thread(new Task("Thread"+i)).start());

 ## Statement Lambda
 .forEach(i -> {
    .......
    .......
    .......
 });


 */
public class ThreadLocalRandomExample {

    private static final class Task implements Runnable {
        private final String name;

        Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {

            // Just a fancy way of saying for i= 1; i <= 10; i++
            // range with end included
            IntStream.rangeClosed(1, 2)
                    // lambda with {..} is called statement lambda
                    .forEach(i -> {
                        String prefix = name + "-" + i;

                        // randomNumber [0, 10). Upper Bound Excluded.
                        int randomNumber = ThreadLocalRandom.current().nextInt(10);
                        System.out.println(prefix + " Excluded[0, 10) " + randomNumber);

                        // randomNumber [1, 10). Upper Bound  Excluded
                        randomNumber = ThreadLocalRandom.current().nextInt(1, 10);
                        System.out.println(prefix + " Included[1, 10) " + randomNumber);

                    });
        }
    }

    public static void main(String[] args) {

        // range with end Exclusive
        IntStream.range(0, 200)
                // example of expression lambda
                .forEach(i -> new Thread(new Task("Thread"+i)).start());

    }
}
