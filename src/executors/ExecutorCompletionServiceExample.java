package executors;

import java.util.concurrent.*;

public class ExecutorCompletionServiceExample {

    // this class will submit 10 tasks to the service
    private static final class Submitter implements Runnable {
        private final CompletionService<String> service;

        Submitter(CompletionService<String> service) {
            this.service = service;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                // Trick to beat final / effective final ??
                int j = i;
                Callable<String> c = () -> "task" + j;
                service.submit(c);
            }
        }
    }

    // this class will consume the results
    private static final class Processor implements Runnable {
        private final CompletionService<String> service;
        private boolean end;

        Processor(CompletionService<String> service) {
            this.service = service;
            end = false;
        }

        public void setEnd(boolean flag) {
            end = flag;
        }

        @Override
        public void run() {
            // consume results until end is not set
            while (!end)
                try {
                    // poll() will return null if the timeout is over and no result is found
                    Future<String> result = service.poll(1, TimeUnit.SECONDS);
                    if (result != null) {
                        System.out.println(result.get());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CompletionService<String> service = new ExecutorCompletionService<String>(executor);
        new Thread(new Submitter(service)).start();
        Processor processor = new Processor(service);
        new Thread(processor).start();
        TimeUnit.SECONDS.sleep(1);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
        processor.setEnd(true);
    }

}
