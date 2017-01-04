package readerwriter;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 # Reader Writer Problem

 At a time many readers can be reading
 At a time only one writer can be writing.


 Notes:
 1. Locks are by default unfair and an unfair lock can cause starvation.
 2. Lock should be locked outside try and released in finally


 There are 3 variations of Reader Writer Problem

 (a) FIFO - Everyone gets in order of request. Readers will be batched together.
 (b) Writer Biased - once a writer requests no more readers are allocated
 (c) Reader biased - If a reader already has lock, it will read even if a writer is waiting.

 */
public class UnbiasedFIFO {


    private static final class Reader implements Runnable {

        private final String name;
        private final ReadWriteLock lock;

        Reader(String name, ReadWriteLock lock) {
            this.name = name;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(name + " requested reading.");
                lock.readLock().lock();
                try {
                    System.out.println( name + " started reading.");
                    // sleep for random ms
                    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                    System.out.println( name + " finished reading.");

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    System.out.println(name + " released lock.");
                    lock.readLock().unlock();
                }
            }
        }
    }

    private static final class Writer implements Runnable {

        private final String name;
        private final ReadWriteLock lock;

        Writer(String name, ReadWriteLock lock) {
            this.name = name;
            this.lock = lock;
        }

        @Override
        public void run() {
            // write 10 times
            for (int i = 0; i < 10; i++) {
                System.out.println(name + " requested writing.");
                lock.writeLock().lock();
                try {
                    System.out.println( name + " started writing.");
                    // sleep for random ms till 10
                    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                    System.out.println( name + " finished writing.");

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    lock.writeLock().unlock();
                    System.out.println(name + " released lock.");
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread[] readers = new Thread[5];
        Thread[] writers = new Thread[5];
        // NOTE: Without the parameter being true, this doesn't guarantee fairness/no starvation
        ReadWriteLock lock = new ReentrantReadWriteLock(true);
        for (int i = 0; i < 5; i++) {
            readers[i] = new Thread(new Reader("reader" + i, lock));
            writers[i] = new Thread(new Writer("writer" + i, lock));
            readers[i].start();
            writers[i].start();
        }

    }

}
