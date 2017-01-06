package misc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**

 Showing divide and conquer using CyclicBarrier.
 And also demonstrates that the barrier is reset automatically once it is reached.


 Properties of CyclicBarrier
 =============================
 1. It needs to know number of threads in advance.
 2. It is reset once it reaches
 3. If one fails, all fail

 */
public class CyclicBarrierExample {

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

        // prove that the barrier is reset once again
        for (int i = 0; i < 100; i++) {
            result[i] = 0;
        }
        for (int i = 0; i < 100; i++) {
            new Thread(new Task(matrix, i, 100, result, barrier)).start();
        }
    }
}
