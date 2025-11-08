import java.util.concurrent.*;
import java.util.*;

public class Main {
    public static final int LENGTH = 12;
    public static final int LENGTH_OF_PART = 4;
    public static void main(String[] args) throws Exception {
        System.out.println("Preparing the array...");

        short[] array = new short[LENGTH];
        CopyOnWriteArraySet<Integer> result = new CopyOnWriteArraySet<Integer>();
        for (int i = 0; i < LENGTH; i++) {
            array[i] = (short) (Math.random() * 11);
            System.out.print(array[i] + " ");
            if (i == LENGTH - 1) {
                System.out.println();
            }
        }

        System.out.print("Start processing the array in");
        for (int i = 3; i >= 0; i--) {
            Thread.sleep(500);
            System.out.print("\n" + i);
            if (i == 0) {
                System.out.println();
            }
        }

        
        long startTime = System.nanoTime();

        ExecutorService executor = Executors.newFixedThreadPool(3);
        int numOfParts = LENGTH / LENGTH_OF_PART;
        for (int i = 0, k = 0; i < numOfParts; i++, k += LENGTH_OF_PART) {
            executor.submit(new MyRunnable(array, k, k + LENGTH_OF_PART, result));
        }

        executor.shutdown();

        while (!executor.isTerminated()) {}

        long stopTime = System.nanoTime();
        double elapsedTime = (stopTime - startTime) / 1e9;
        System.out.println("Program was executed for " + elapsedTime + "s");

        System.out.println("Result: ");
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next() + " ");
        }


    }
}
