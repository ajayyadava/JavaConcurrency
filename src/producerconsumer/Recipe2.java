package producerconsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
  # Producer-Consumer Example using BlockedQueue


For problem statement see Example1


 Notes:
 =======



 */

public class Recipe2 {

    private static final class Producer implements Runnable {
        private final String name;
        private final BlockingQueue<String> buffer;
        private final int count;

        public Producer(String name, BlockingQueue<String> buffer, int count) {
            this.name = name;
            this.buffer = buffer;
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                String message = name + " : " + i;
                try {
                    System.out.println("message = " + message);
                    buffer.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final class Consumer implements Runnable {
        private final String name;
        private final BlockingQueue<String> buffer;
        private final int count;

        public Consumer(String name, BlockingQueue<String> buffer, int count) {
            this.name = name;
            this.buffer = buffer;
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                try {
                    String message = buffer.take();
                    System.out.println(name + message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> buffer = new ArrayBlockingQueue<>(2);

        Thread p1 = new Thread(new Producer("producer1", buffer, 4));
        Thread p2 = new Thread(new Producer("producer2", buffer, 4));
        Thread c1 = new Thread(new Consumer("consumer1", buffer, 4));
        Thread c2 = new Thread(new Consumer("consumer2", buffer, 4));
        p1.start(); c1.start(); p2.start(); c2.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Final Buffer size: " + buffer.size());
    }


}
