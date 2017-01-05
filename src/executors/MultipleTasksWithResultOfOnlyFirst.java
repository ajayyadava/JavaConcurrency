package executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultipleTasksWithResultOfOnlyFirst {
    private static final class Task implements Callable<String> {
        private final int count;
        Task(int count) {
            this.count = count;
        }
        @Override
        public String call() throws Exception {
            // make first callable sleep more
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100 - 10 * count));
            return "result of " + count;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Note: submit method is not available on Executor interface; only on ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Callable<String>> results = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            results.add(new Task(i));
        }
        String resultAny = executor.invokeAny(results);
        System.out.println(resultAny);
        executor.shutdown();
    }
}
