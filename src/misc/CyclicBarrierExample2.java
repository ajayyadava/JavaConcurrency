package misc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**

 Note: that each thread is calling the await() twice;
 if the await acts like countDown in latch, only half results will be populated.
 However, what it effectively does is
 - All threads call await() and are suspended till 100 times await() has been called.
 - Once all 100 threads have been awaited(), then grouper is run
 - cyclic barrier is reset automatically and process is repeated again.

 */
public class CyclicBarrierExample2 {

    private static final class Task implements Runnable {

        private final int row;
        private final int[][] matrix;
        private final int cols;
        private final int[] result;
        private final CyclicBarrier barrier;

        Task(int[][] matrix, int row, int cols, int[] result, CyclicBarrier barrier) {
            this.matrix = matrix;
            this.row = row;
            this.cols = cols;
            this.result = result;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            int ans = matrix[row][0];
            for (int i = 0; i < cols; i++) {
                ans = Math.max(ans, matrix[row][i]);
            }
            result[row] = ans;
            try {
                barrier.await();
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private static final class Grouper implements Runnable {
        private final int[] result;
        Grouper(int[] result) {
            this.result = result;
        }
        @Override
        public void run() {
            int sum = 0;
            for (int i = 0; i < result.length; i++) {
                sum += result[i];
            }
            System.out.printf("Sum of max: %d\n", sum);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[][] matrix = new int[100][100];
        int[] result = new int[100];
        CyclicBarrier barrier = new CyclicBarrier(100, new Grouper(result));
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                matrix[i][j] = i + j + 1;
            }
        }

        for (int i = 0; i < 100; i++) {
            new Thread(new Task(matrix, i, 100, result, barrier)).start();
        }

        TimeUnit.SECONDS.sleep(1);
        int sum = 0;
        for (int i = 0; i < result.length; i++) {
            sum += result[i];
        }
        System.out.println(sum);

    }

}
