package forkjoin;

import java.util.Arrays;
import java.util.concurrent.*;

/**

 Demonstrate
 - how to implement divide and conquer using ForkJoinPool.
 - how to group results from sub-tasks in ForkJoinPool()
 - how to use ForkJoinTask.invokeAll()

 */
public class ForkJoinExample2 {
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
                invokeAll(t1, t2);
                try {
                    // Note: It will reach here only when the result is available.
                    sum = t1.get() + t2.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
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
