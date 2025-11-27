import java.util.*;
import java.util.concurrent.*;

public class AsyncArray {
    static final boolean IS_CPU_BOUND_TASK = false;
    static final int LEFT = -10;
    static final int RIGHT = 10;
    static final int ARR_SIZE = 10;
    public static void main(String[] args) throws Exception {

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            if (IS_CPU_BOUND_TASK) cpuBoundTask();
            System.out.println("\nSUPPLY ASYNC in " + Thread.currentThread().getName());
            ArrayList<Integer> array = new ArrayList<>();
            for (int i = 0; i < ARR_SIZE; i++) {
                array.add((int)(Math.random() * Math.abs(RIGHT - LEFT + 1)) + LEFT);
            }
            System.out.print("The array has just been created: ");
            printArray(array);
            long end = System.nanoTime();
            System.out.print("\nTime: " + (end - start) / 1e6 + "ms");
            return array;
        })
        
        .thenApplyAsync(array -> {
            long start = System.nanoTime();
            if (IS_CPU_BOUND_TASK) cpuBoundTask();
            System.out.print("\n\nTHEN APPLY ASYNC in " + Thread.currentThread().getName());
            for (int i = 0; i < array.size(); i++) {
                array.set(i, array.get(i) + 10);
            }
            printArray(array);
            long end = System.nanoTime();
            System.out.print("\nTime: " + (end - start) / 1e6 + "ms");
            return array;
        })
        
        .thenApplyAsync(array -> {
            long start = System.nanoTime();
            if (IS_CPU_BOUND_TASK) cpuBoundTask();
            System.out.print("\n\nTHEN APPLY ASYNC in " + Thread.currentThread().getName());
            ArrayList<Double> floatArray = new ArrayList<>();
            for (Integer element : array) {
                floatArray.add((double)element / 2);
            }
            long end = System.nanoTime();
            System.out.println("\nTime: " + (end - start) / 1e6 + "ms");
            return floatArray;
        })
        
        .thenAcceptAsync(array -> {
            long start = System.nanoTime();
            if (IS_CPU_BOUND_TASK) cpuBoundTask();
            System.out.print("\nTHEN ACCEPT ASYNC in " + Thread.currentThread().getName());
            System.out.print("\nThe result of division: ");
            printArray(array);
            long end = System.nanoTime();
            System.out.println("\nTime: " + (end - start) / 1e6 + "ms");
        });

        // for (int i = 0; i < 10; i++) {
        //     Thread.sleep(1);
        //     System.out.println("Stop for a 1ms");
        // }

        future.join();
        System.out.println("\nTHE END.\n");
    }

    public static <T> void printArray(ArrayList<T> array) {
        System.out.print("\nArray: [  ");
        for (T element : array) {
            System.out.print(element + "  ");
        }
        System.out.print("]");
    }

    public static void cpuBoundTask() {
        for (int i = 0; i < 10000000; i++) {
            Math.cos(Math.sqrt(Math.sin(Math.random())));
        }
    }
}


