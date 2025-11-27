import java.util.*;
import java.util.concurrent.*;

public class AsyncSequence {
    static final int SIZE = 20;
    static final int LEFT = 0;
    static final int RIGHT = 5;
    static final int PRECISION = (int)1e2;
    public static void main(String[] args) {

        System.out.println("\nExample sequence: a1, a2, a3, ..., a(n)");
        System.out.println("Result should be: (a2 - a1) * (a3 - a2) * ... * (a(n) - a(n-1)");

        ArrayList<String> results = new ArrayList<>();
    
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            ArrayList<Double> sequence = new ArrayList<>();
            for (int i = 0; i < SIZE; i++) {
                double number = Math.random() * Math.abs(RIGHT - LEFT + 1) + LEFT;
                int tempNumber = (int)((double)number * PRECISION);
                sequence.add((double)tempNumber / PRECISION);
            }
            long end = System.nanoTime();
            double elapsedTime = (end - start) / 1e6;
            String result = "Elapsed time for SUPPLY ASYNC in " + Thread.currentThread().getName() + " is ";
            result += elapsedTime + " ms";
            results.add(result);
            return sequence;
        })
        
        .thenApplyAsync(sequence -> {
            long start = System.nanoTime();
            double res = 1.0;
            for (int i = 0; i < sequence.size() - 1; i += 1) {
                double diff = sequence.get(i + 1) - sequence.get(i);
                res *= diff;
            }
            res = (double)((int)(res * PRECISION)) / PRECISION;
            sequence.add(res);
            long end = System.nanoTime();
            double elapsedTime = (end - start) / 1e6;
            String result = "Elapsed time for THEN APPLY ASYNC in " + Thread.currentThread().getName() + " is ";
            result += elapsedTime + " ms";
            results.add(result);
            return sequence;
        })

        .thenAcceptAsync(sequence -> {
            long start = System.nanoTime();
            double result = (double)((int)(sequence.remove(sequence.size() - 1) * PRECISION)) / PRECISION;
            System.out.print("\nStart sequence:\n[  ");
            for (double element : sequence) {
                System.out.print(element + "  ");
            }
            System.out.println("]\n");
            System.out.println("Result: " + result + "\n");
            long end = System.nanoTime();
            double elapsedTime = (end - start) / 1e6;
            String res = "Elapsed time for THEN ACCEPT ASYNC in " + Thread.currentThread().getName() + " is ";
            res += elapsedTime + " ms";
            results.add(res);
        })
        
        .thenRunAsync(() -> {
            for (String result : results) {
                System.out.println(result);
            }
            System.out.println();
        });

        future.join();
    }
}


