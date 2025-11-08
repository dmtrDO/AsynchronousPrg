
import java.util.concurrent.*;
import java.util.*;

public class Main {
    /////////////////////////////////////////////////////////
    public static final boolean SHOW_INFO = true;
    public static final boolean ASYNC = true;
    public static final int LENGTH = (int)(24);
    public static final int LENGTH_OF_PART = (int)(4);
    /////////////////////////////////////////////////////////
    public static void main(String args[]) throws Exception {
        System.out.println("Preparing the array...");

        short[] array = new short[LENGTH];
        short[] result = new short[LENGTH / 2];
        for (int i = 0; i < LENGTH; i++) {
            array[i] = (short) (Math.random() * 101);
            if (SHOW_INFO)
                System.out.print(array[i] + " ");
            if (i == LENGTH - 1) {
                if (SHOW_INFO)
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

        if (ASYNC) {
            result = async(array);
        } else {
            result = noAsync(array);
        }

        long stopTime = System.nanoTime();
        double elapsedTime = (stopTime - startTime) / 1e9;
        System.out.println("Program was executed for " + elapsedTime + "s");

        if (SHOW_INFO) System.out.println("Result: ");
        for (int i = 0; i < result.length; i++) {
            if (SHOW_INFO) System.out.print(result[i] + " ");
        }
    }

    public static short[] async(short[] array) throws Exception {
        short[] result = new short[array.length / 2];
        ExecutorService executorService = Executors.newFixedThreadPool(3); 
        ArrayList<Future<short[]>> futures = new ArrayList<>();
        int numOfParts = LENGTH / LENGTH_OF_PART;
        for (int i = 0, k = 0; i < numOfParts; i++, k += LENGTH_OF_PART) {
            futures.add(executorService.submit(new MyCallable(array, k, k + LENGTH_OF_PART)));
        }
        ArrayList<Integer> missedResults = new ArrayList<Integer>();
        for (int i = 0, j = 0; i < futures.size(); i++) {
            if (futures.get(i).isDone()) {
                if (futures.get(i).isCancelled()) {
                    if (SHOW_INFO) System.out.println("The task " + i + " was cancelled");
                } else {
                    short[] subArray = futures.get(i).get();
                    for (int k = 0; k < subArray.length; k++, j++) {
                        result[j] = subArray[k];
                    }
                }
            } else {
                missedResults.add(i);
            }
        }
        for (int i = 0, j = result.length - 1; i < missedResults.size(); i++) {
            short[] subArray = futures.get(missedResults.get(i)).get();
            for (int k = 0; k < subArray.length; k++, j--) {
                result[j] = subArray[k];
            }
        }

        // for (int i = 0, j = 0; i < futures.size(); i++) {
        //     short[] subArray = futures.get(i).get();
        //     for (int k = 0; k < subArray.length; k++, j++) {
        //         result[j] = subArray[k];
        //     }
        // }

        executorService.shutdown();
        return result;
    }

    public static short[] noAsync(short[] nums) {
        short[] result = new short[nums.length / 2];
        if (Main.SHOW_INFO)
            System.out.println(Thread.currentThread().getName() + " is processing...");
        short counter, num1, num2;
        counter = num1 = num2 = 0;
        for (int i = 0, j = 0; i < nums.length; i++) {
            if (counter == 0) { 
                num1 = nums[i]; 
            } else {
                num2 = nums[i];
            }
            if (counter == 1) {
                result[j] = (short)(num1 * num2);
                //double a = Math.sqrt(num1 + i) * Math.sin(num2 + i);
                //double b = Math.sqrt(num1 + i) * Math.sin(num2 + i);
                j++;
                counter = 0;
                continue;
            }
            counter++;
        }
        return result;
    }
}
