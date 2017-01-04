package readerwriter;


import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**

 Once a writer requests, no more readers are entertained.

You need to keep a track of all the write requests

 You can't do it with lock.lock() / lock.reader.lock() only because they actually place request for the lock
 and you need a mechanism which will make sure that the request is placed only when some conditions are met

 this is the example of "Using Conditions to coordinate threads" and there are 2 ways to do it
 1. synchronized + wait-notifyAll()
 2. Lock.conditions.new condition()

 All problems that can be solved by 1 (wait-notifyAll) can be solved by other as well.

 However, the only difference is that synchronized doesn't allow multiple reads simultaneously. Only one thread is
 allowed to enter the critical section.

 Difference 2. Using lock.conditions() you can wake up only a particular group of threads unlike notifyAll()


 Caution:
 This class should be used only to maintain reads and writes requests. Reading and writing should be done in other class
 Only then it will allow multiple parallel reads.

 If you change the requestX method to do read/write.
 It doesn't allow parallel reads because it uses synchronized which allows only one
 thread to enter the critical section so two readers can't be granted access.

 */


public class BiasedWriter {

    private int numberOfReaders;
    private int numberOfWriters;
    private int pendingWriteRequests;

    BiasedWriter() {
        this.numberOfReaders = 0;
        this.numberOfWriters = 0;
        this.pendingWriteRequests = 0;
    }

    synchronized void requestRead(String name) throws InterruptedException {
        while (numberOfWriters > 0 || pendingWriteRequests > 0) {
            wait();
        }
        System.out.printf("Reader %s has been granted access.\n", name);
        numberOfReaders++;
        System.out.printf("Current State: Readers: %d, Writers: %d, " +
                        "Pending Write Requests: %d\n", numberOfReaders,
                numberOfWriters, pendingWriteRequests);
    }

    public synchronized void releaseRead(String name) {
        System.out.printf("Reader: %s has finished reading.\n", name);
        numberOfReaders--;
        System.out.printf("Current State: Readers: %d, Writers: %d, " +
                        "Pending Write Requests: %d\n", numberOfReaders,
                numberOfWriters, pendingWriteRequests);
        notifyAll();
    }


    public synchronized void requestWrite(String name) throws InterruptedException {
        pendingWriteRequests++;
        while (numberOfReaders > 0 || numberOfWriters > 0) {
            wait();
        }
        pendingWriteRequests--;
        numberOfWriters++;
        System.out.printf("Writer: %s has been granted access.\n", name);
        System.out.printf("Current State: Readers: %d, Writers: %d, " +
                        "Pending Write Requests: %d\n", numberOfReaders,
                numberOfWriters, pendingWriteRequests);
    }

    public synchronized void releaseWrite(String name) {
        System.out.printf("Writer: %s has finished writing.\n", name);
        numberOfWriters--;
        System.out.printf("Current State: Readers: %d, Writers: %d, " +
                        "Pending Write Requests: %d\n", numberOfReaders,
                numberOfWriters, pendingWriteRequests);
        notifyAll();
    }

    public static void main(String[] args) {
        final BiasedWriter readerWriterRegistry = new BiasedWriter();

        // create 2 reader threads - which will keep doing requestRead()
        for (int i = 0; i < 2; i++) {
            final String readerName = "Reader" + i;
            final String writerName = "Writer" + i;

            new Thread(() ->{
                for (int j = 0; j < 4; j++) {
                    try {
                        // Note: Since the lock is reenterant, writer 1 will finish
                        // all 4 of its writes first - so a greedy writer may cause
                        // starvation. You can solve this by using a fair lock
                        // instead of synchronized
                        readerWriterRegistry.requestRead(readerName);
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100));
                        readerWriterRegistry.releaseRead(readerName);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // and 2 writer threads which will keep doing requestWrite()
            new Thread(() ->{
                for (int j = 0; j < 4; j++) {
                    try {
                        readerWriterRegistry.requestWrite(writerName);
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100));
                        readerWriterRegistry.releaseWrite(writerName);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
