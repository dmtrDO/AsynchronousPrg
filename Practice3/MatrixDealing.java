
import java.util.concurrent.*;
import java.util.*;

public class MatrixDealing {
    // range of generated numbers
    private static final int LEFT_VALUE = -10000000;
    private static final int RIGHT_VALUE = 10000000;

    // if the number of columns or rows is more than this limit, then we don't show the matrix
    private static final int LIMIT_NUBMER_TO_SHOW_MATRIX = 10;

    private static final boolean IS_ASYNC = true;

    private static final int NUMBER_OF_THREADS = 10;

    private static final boolean IS_CPU_BOUND = false;

    private static final int MIN_RANGE_TO_STOP_RECURSION = 100;

    private static class MatrixElementFinder implements Runnable {
        private int[][] matrix;
        private int start;
        private int end;
        
        public MatrixElementFinder(int[][] matrix, int start, int end) {
            this.matrix = matrix;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            int numRows = matrix.length;
            int numCols = matrix[0].length;

            for (int index = start; index < end; index++) {
                int i = index / numCols;
                int j = index % numCols;

                if (IS_CPU_BOUND) {
                    @SuppressWarnings("unused")
                    double cpuBound1 = Math.sin(i) * Math.cos(i);
                    @SuppressWarnings("unused")
                    double cpuBound2 = Math.sqrt(i % 1000);
                }

                if (i < numRows && j < numCols && matrix[i][j] == i + j) {
                    System.out.printf("Found: matrix[%d][%d] = %d%n", i, j, matrix[i][j]);
                }
            } 
        }
    }

    private static class WorkDealingPool {
        private int numOfThreads;

        public WorkDealingPool(int numOfThreads) {
            this.numOfThreads = numOfThreads;
        }

        private void invoke(int[][] matrix, int length) {
            ArrayList<ExecutorService> executors = new ArrayList<>();
            int tasksForThread = length / numOfThreads / MIN_RANGE_TO_STOP_RECURSION;

            int start = 0;
            for (int i = 0; i < numOfThreads; i++) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                for (int j = 0; j < tasksForThread; j++) {
                    executor.submit(new MatrixElementFinder(matrix, start, start + MIN_RANGE_TO_STOP_RECURSION));
                    start += MIN_RANGE_TO_STOP_RECURSION;
                }
                executors.add(executor);
            }
            for (ExecutorService exec : executors) {
                exec.shutdown();
                try {
                    if (!exec.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                        exec.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    exec.shutdownNow();
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] matrix = initMatrix(sc);
        System.out.println("Start executing...");
        long startTime = System.nanoTime();

        if (IS_ASYNC) {
            System.out.println("Load-balancing technique: work dealing");
           
            WorkDealingPool workDealingPool = new WorkDealingPool(NUMBER_OF_THREADS);
            workDealingPool.invoke(matrix, matrix.length * matrix[0].length);
        } else {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {

                    if (IS_CPU_BOUND) {
                        @SuppressWarnings("unused")
                        double cpuBound1 = Math.sin(i) * Math.cos(i);
                        @SuppressWarnings("unused")
                        double cpuBound2 = Math.sqrt(i % 1000);
                    }

                    if (i + j == matrix[i][j]) {
                        System.out.printf("Found: matrix[%d][%d] = %d%n", i, j, matrix[i][j]);
                    }
                }
            }
        }

        long endTime = System.nanoTime();
        System.out.println("The program has been executed for " + (endTime - startTime) / 1e9 + "s");
    }

    public static int[][] initMatrix(Scanner sc) {
        int numOfRows = readNum("Input the number of rows of the matrix: ", sc);
        int numOfColumns = readNum("Input the number of columns of the matrix: ", sc);
        int firstElement = readNum("Input the value of the first element of the matrix: ", sc);
        int lastElement = readNum("Input the value of the last element of the matrix: ", sc);
        int[][] matrix = new int[numOfRows][numOfColumns];
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                matrix[i][j] = (int)(Math.random() * Math.abs(RIGHT_VALUE - LEFT_VALUE + 1) + LEFT_VALUE);
            }
        }
        matrix[0][0] = firstElement;
        matrix[numOfRows - 1][numOfColumns - 1] = lastElement;
        if (numOfRows <= LIMIT_NUBMER_TO_SHOW_MATRIX && numOfColumns <= LIMIT_NUBMER_TO_SHOW_MATRIX) {
            for (int i = 0; i < numOfRows; i++) {
                for (int j = 0; j < numOfColumns; j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }
        }
        return matrix;
    }

    public static int readNum(String message, Scanner sc) {
        while (true) {
            try {
                System.out.print(message);
                int res = sc.nextInt();
                if (res < 1) throw new Exception("The value must be more than zero");
                return res;
            } catch (Exception e) {
                System.err.println("\nError input:\n" + e + "\nTry again: ");
                sc.nextLine();
            }
        }
    }

}
