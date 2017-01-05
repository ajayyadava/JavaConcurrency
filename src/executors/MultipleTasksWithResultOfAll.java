package executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultipleTasksWithResultOfAll {
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
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Callable<String>> results = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            results.add(new Task(i));
        }
        List<Future<String>> resultAll = executor.invokeAll(results);
        for (Future<String> result : resultAll) {
            System.out.println(result.get());
        }
        executor.shutdown();
    }

}
