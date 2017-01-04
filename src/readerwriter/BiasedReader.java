package readerwriter;


import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**

 If a reader requests and another reader has lock, then it will be given access even if
 there is a writer who has requested access before the new reader.



 */


public class BiasedReader {

    private int numberOfReaders;
    private int numberOfWriters;

    BiasedReader() {
        this.numberOfReaders = 0;
        this.numberOfWriters = 0;
    }

    synchronized void requestRead(String name) throws InterruptedException {
        while (numberOfWriters > 0) {
            wait();
        }
        System.out.printf("Reader %s has been granted access.\n", name);
        numberOfReaders++;
        System.out.printf("Current State: Readers: %d, Writers: %d\n", numberOfReaders, numberOfWriters);
    }

    public synchronized void releaseRead(String name) {
        System.out.printf("Reader: %s has finished reading.\n", name);
        numberOfReaders--;
        System.out.printf("Current State: Readers: %d, Writers: %d\n", numberOfReaders, numberOfWriters);
        notifyAll();
    }


    public synchronized void requestWrite(String name) throws InterruptedException {
        while (numberOfReaders > 0 || numberOfWriters > 0) {
            wait();
        }
        numberOfWriters++;
        System.out.printf("Writer: %s has been granted access.\n", name);
        System.out.printf("Current State: Readers: %d, Writers: %d\n", numberOfReaders, numberOfWriters);
    }

    public synchronized void releaseWrite(String name) {
        System.out.printf("Writer: %s has finished writing.\n", name);
        numberOfWriters--;
        System.out.printf("Current State: Readers: %d, Writers: %d\n", numberOfReaders, numberOfWriters);
        notifyAll();
    }

    public static void main(String[] args) {
        final BiasedReader readerWriterRegistry = new BiasedReader();

        // create 2 reader threads - which will keep doing requestRead()
        for (int i = 0; i < 2; i++) {
            final String readerName = "Reader:" + i;
            final String writerName = "Writer:" + i;

            new Thread(() -> {
                for (int j = 0; j < 4; j++) {
                    try {
                        readerWriterRegistry.requestRead(readerName);
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100));
                        readerWriterRegistry.releaseRead(readerName);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            new Thread(() -> {
                for (int k = 0; k < 4; k++) {
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
