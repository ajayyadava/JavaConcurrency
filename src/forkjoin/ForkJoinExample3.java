package forkjoin;

import java.util.Arrays;
import java.util.concurrent.*;

/**

 Demonstrate
 - Usage of Asynchronous task execution using
 ForkJoinTask.fork() and ForkJoinTask.join()
 
 */
public class ForkJoinExample3 {
    private static final class Task extends RecursiveTask<Long> {
        private final int[] arr;
        private final int start;
        private final int end;
        Task(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }
        @Override
        protected Long compute() {
            long sum = 0;
            if (end - start > 20) {
                int mid = start + (end - start ) / 2;
                Task t1 = new Task(arr, start, mid);
                Task t2 = new Task(arr, mid+1, end);
                //NOTE: t1 will be executed in another thread and this thread will continue to next statement
                t1.fork();
                t2.fork();

                // get result from both t1 and t2 - like get()
                // Note: It is not necessary that the result will be available when you reach here.
                sum = t1.join() + t2.join();
            } else {
                System.out.printf("%s adding from %d to %d\n", Thread.currentThread().getName(), start, end);
                for (int i = start; i <= end; i++) {
                    sum += arr[i];
                }
            }
            return sum;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int[] arr = new int[100];
        Arrays.fill(arr,1);
        ForkJoinTask<Long> task = new Task(arr,0,99);
        ForkJoinPool pool = new ForkJoinPool();
        pool.execute(task);
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);
        System.out.println(task.get());
    }
}
