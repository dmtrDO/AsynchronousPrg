import java.util.concurrent.CopyOnWriteArraySet;

public class MyRunnable implements Runnable {

    private CopyOnWriteArraySet<Integer> result;
    private short[] nums;
    private final int start;
    private final int end;

    public MyRunnable(short[] array, int start, int end, CopyOnWriteArraySet<Integer> result) {
        this.result = result;
        this.nums = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is processing...");
        short counter, num1, num2;
        counter = num1 = num2 = 0;
        for (int i = start; i < end; i++) {
            if (counter == 0) { 
                num1 = nums[i]; 
            } else {
                num2 = nums[i];
            }
            if (counter == 1) {
                result.add(num1 * num2);
                counter = 0;
                continue;
            }
            counter++;
        }
    }
}