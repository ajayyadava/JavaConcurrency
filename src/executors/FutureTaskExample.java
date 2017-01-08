package executors;

import java.util.concurrent.*;

// FutureTask will wrap an executable task( only Callable supported?) and will be submitted to the executor.
public class FutureTaskExample {
    private static final class Task implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("Callable executed");
            return "Task Result";
        }
    }

    private static final class Callback extends FutureTask<String> {
        Callback(Callable<String> callable) {
            super(callable);
        }
        @Override
        protected void done() {
            try {
                String result = this.get();
                System.out.println("Executing callback for task with result: " + result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Task t = new Task();
        Callback futureTask = new Callback(t);
        executor.submit(futureTask);
        executor.shutdown();
    }
}
