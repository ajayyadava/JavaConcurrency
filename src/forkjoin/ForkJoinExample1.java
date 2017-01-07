package forkjoin;


import java.util.concurrent.*;

/**

 Demonstrate divide and conquer approach using fork() and join() methods.

 1. Unlike Executors, Fork-Join Pool doesn't accept runnable or callable.
 It only accepts concrete implementations of
 **ForkJoinTask** which are
        a. RecursiveAction  - doesn't return result
        b. RecursiveTask    - returns result
 2. If you are extending/using RecursiveAction which doesn't return a result, you still have to use
 generic type Void for your class to avoid warning.

 3. To submit a ForkJoinTask to a forkjoin pool - you have following options

    - pool.execute(forkJoinTask)    - Fire asynchronously and forget - good for RecursiveAction types.
    - pool.submit(forkJoinTask)
      forkJoinTask.get()            - execute a task asynchronously and call .get() on it like Future.get()
    - String result = pool.invoke(ForkJoinTask<String>)             - execute task and get result.
 */
public class ForkJoinExample1 {

    private static final class Task extends RecursiveAction {
        @Override
        public void compute() {
            System.out.println("Task called");
        }
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ForkJoinPool pool = new ForkJoinPool();
        int[][] matrix = new int[5][5];
        ForkJoinTask<Void> forkJoinAction = new RecursiveAction() {
            @Override
            protected void compute() {
                System.out.println("Action finished.");
            }
        };
        // async execution
        pool.execute(forkJoinAction);

        ForkJoinTask<String> forkJoinTask = new RecursiveTask<String>() {
            @Override
            protected String compute() {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "Task result";
            }
        };

        // await and obtain result
        String  result = pool.invoke(forkJoinTask);
        System.out.println("result = " + result);


        // arrange async execution and obtain future, print the result
        // ForkJoinTask<String> future =
        pool.submit(forkJoinTask);

        // System.out.println(future.get());
        System.out.println(forkJoinTask.get());


        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);
    }
}
