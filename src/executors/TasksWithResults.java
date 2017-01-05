package executors;

import java.util.concurrent.*;

public class TasksWithResults {

    private static final class Task implements Callable<String> {
        private final String name;
        Task(String name) {
            this.name = name;
        }
        @Override
        public String call() throws Exception {
            return "result of " + name;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Note: submit method is not available on Executor interface only on ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<String> result = executor.submit(new Task("task1"));
        System.out.println(result.get());
        executor.shutdown();
    }

}
