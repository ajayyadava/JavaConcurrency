package producerconsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**

 Illustrates Producer Consumer using synchronized-wait mechanism

 # Producer-Consumer Problem
  Producer can't produce if the queue/buffer is full
  Consumer can't consume if the queue/buffer is empty
  Events/Messages should be consumed in FIFO fashion.



 ## Common mistakes:
  1. Since synchronization has to be done on a common lock - we use buffer

  2. Wait and notifyAll can be called only once you have the lock otherwise they throw the illegalMonitorStateException

  3. Because of 1 and 2. `wait()` and `notifyAll()` should be called as `buffer.wait()` and `buffer.notifyAll()` instead
 of just `wait()` and `notifyAll()`;

  4. The object on which the lock is taken(in this case buffer) should be declared as final.

  5. wait should be called inside a while loop as when the thread wakes up might not have

  6. Notice the flow:

 A thread takes the lock, checks the condition and finds that it can't proceed so calls wait,
 then other thread gets the lock, checks that it meets conditions, so does some work, calls notify.

 So the thread gives up the lock when wait is called.

 ** This is the difference between sleep() and wait(); Calling wait() releases all locks while calling sleep doesn't **


 Quiz: If a thread takes two locks and calls wait(), what happens? Will both locks be released?
 ------------------------------------------------------------------
 Ans: Only the lock on which you have called the wait() will be released, by default it is this.wait();


  7. Ideal datastructure for the producer-consumer problem is a bounded queue, but we have used queue here because we
  wanted to illustrate ""Using Conditions in Synchronized Code"" but bounded queue implementations are thread safe and
  allow blocking operations which take care of the coordination between threads using blocking operations

  8. Variations of add/ remove  -- (offer and poll() etc. are available in Queue interface but not applicable here
  as the underlying datastructure that we have used is LinkedList, so

 */
public class UsingSynchronizedAndWait {

    private static class Producer implements Runnable {

        private final int count;

        /**
         * Name of the producer.
         * Each producer/consumer thread is given a unique name to distinguish
         * the messages produced by the thread.
         */
        private String name;

        /**
         * Buffer from which the consumer consumes.
         * This is shared between the <code>Producer</code> and the <code>Consumer</code>
         */
        private final Queue<String> buffer;

        /**
         * Maximum capacity of the buffer.
         */
        private int capacity;

        public Producer(String name, Queue<String> buffer, int capacity, int count) {
            this.name = name;
            this.buffer = buffer;
            this.capacity = capacity;
            this.count = count;
        }


        @Override
        public void run() {
               // produce 20 messages from each producer
                for (int i=1; i<=count; i++) {
                    produce(i);
                }
        }

        private void produce(int i) {
            // synchronize on an object common to all producers and consumers
            synchronized (buffer) {
                // NOTE: don't use if condition here
                while (buffer.size() == capacity) {
                    try {
                        buffer.wait();  //NOTE: wait and notifyAll() has to be on the lock object and not just wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String message = name + ": message: " + i;
                System.out.println(message);

                //NOTE: could be add() - will throw unchecked exception IllegalStateException
                // as the underlying ds is LinkedList<> which is not bounded
                buffer.offer(message);
                buffer.notifyAll();
            }
        }
    }

    private static class Consumer implements Runnable {

        private final int count;
        private String name;

        // NOTE: the object on which lock is taken should be declared final
        private final Queue<String> buffer;

        public Consumer(String name, Queue<String> buffer, int count) {
            this.name = name;
            this.buffer = buffer;
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 1; i <= count; i++) {
                consume();
            }
        }

        private void consume() {
            synchronized (buffer) {
                while (buffer.size() == 0) {
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Note: Shouldn't be remove() - throws unchecked exception IllegalStateException if empty
                // poll will just return null if empty!!
                String message = buffer.poll();
                System.out.println(name + " : " + message);
                buffer.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        Queue<String> buffer = new LinkedList<>();
        // create 2 producers
        Thread p1 = new Thread(new Producer("producer1", buffer, 2, 4));
        Thread p2 = new Thread(new Producer("producer2", buffer, 2, 4));

        // create 2 consumers
        Thread c1 = new Thread(new Consumer("consumer1", buffer, 4));
        Thread c2 = new Thread(new Consumer("consumer2", buffer, 4));
        // start the producer, start the consumer
        p1.start();
        c1.start();
        p2.start();
        c2.start();

        // sleep for 1 second to give chance to all threads to finish
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Final Buffer size: %d", buffer.size());
    }

}
