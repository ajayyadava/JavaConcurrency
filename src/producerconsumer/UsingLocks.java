package producerconsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *

 1. You can't call the methods like await() on a condition unless you hold the lock on the lock of the condition.

 2. Conditions have both notifyAll() and signalAll() - remember that notifyAll() is inherited from Object and works on
 this so the correct method to be called is signalAll()

 3. We could have followed the SynchronizedAndWait approach i.e.
 created a common buffer and pass it to Producer and Consumer along with the lock but that will
 make the code bloated

 so we have followed the BlockingQueue approach.

 */
public class UsingLocks {
    private static final class Buffer {
        private final int capacity;
        private final ReentrantLock lock;
        private final Condition isFull;
        private final Condition isEmpty;
        private final Queue<String> queue;

        Buffer(int capacity) {
            this.capacity = capacity;
            this.queue = new LinkedList<>();
            this.lock = new ReentrantLock();
            this.isFull = lock.newCondition();
            this.isEmpty = lock.newCondition();
        }

        public void produce(String message) {
            lock.lock();
            try {
                while (queue.size() == capacity) {
                    isFull.awaitUninterruptibly();
                }
                queue.add(message);
                System.out.printf("%s produced message:%s \n", Thread.currentThread().getName(), message);
                isEmpty.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void consume() {
            lock.lock();
            try {
                while (queue.size() == 0) {
                    isEmpty.awaitUninterruptibly();
                }
                // Simulate producing by sleeping
                String message = queue.remove();
                System.out.printf("%s consumed message. %s\n", Thread.currentThread().getName(), message);
                isFull.signalAll();
            }finally {
                lock.unlock();
            }
        }

        public int getSize() {
            return queue.size();
        }
    }

    private static final class Producer implements Runnable {
        private final int count;
        private final String name;
        private final Buffer buffer;

        Producer(String name, int count, Buffer buffer) {
            this.name = name;
            this.count = count;
            this.buffer = buffer;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(name);
            for (int i = 0; i < count; i++) {
                buffer.produce(name + ": message:" + i);
            }
        }
    }

    private static final class Consumer implements Runnable {
        private final int count;
        private final String name;
        private final Buffer buffer;

        Consumer(String name, int count, Buffer buffer) {
            this.name = name;
            // Note: you can't do Thread.currentThread.setName(name) here as constructor is executed in main thread
            this.count = count;
            this.buffer = buffer;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(name);
            for (int i = 0; i < count; i++) {
                buffer.consume();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Buffer buffer = new Buffer(2);
        for (int i = 0; i < 2; i++) {
            new Thread(new Producer("p" + i, 4, buffer)).start();
            new Thread(new Consumer("c" + i, 4, buffer)).start();
        }
        TimeUnit.SECONDS.sleep(1);
        System.out.printf("Final buffer size is: %d\n", buffer.getSize());
    }
}
