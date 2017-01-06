package misc;

import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicArray {
    public static final class Adder implements Runnable {
        private final AtomicLongArray array;
        Adder(AtomicLongArray array) {
            this.array = array;
        }

        @Override
        public void run() {
            for (int i = 0; i < array.length(); i++) {
                array.addAndGet(i, 2 * i);
            }
        }
    }

    public static final class Subtracter implements Runnable {
        private final AtomicLongArray array;
        Subtracter(AtomicLongArray array) {
            this.array = array;
        }

        @Override
        public void run() {
            for (int i = 0; i < array.length(); i++) {
                array.addAndGet(i, -2 * i);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicLongArray array = new AtomicLongArray(35);

        Thread[] adders = new Thread[100];
        Thread[] subtracters = new Thread[100];
        for (int i = 0; i < 100; i++) {
            adders[i] = new Thread(new Adder(array));
            subtracters[i] = new Thread(new Subtracter(array));

            adders[i].start();
            subtracters[i].start();
        }

        for (int i = 0; i < 100; i++) {
            adders[i].join();
            subtracters[i].join();
        }

        for (int i = 0; i < array.length(); i++) {
            System.out.printf("%d, ", array.get(i));
        }
    }
}
