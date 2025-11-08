
 import java.util.concurrent.*;

public class MyCallable implements Callable<short[]> {
    
    private short[] nums;
    private final int start;
    private final int end;

    public MyCallable(short[] nums, int start, int end) {
        this.nums = nums;
        this.start = start;
        this.end = end;
    }
    
    @Override
    public short[] call() {
        if (Main.SHOW_INFO)
            System.out.println(Thread.currentThread().getName() + " is processing...");
        short[] res = new short[(end - start) / 2];
        short counter, num1, num2;
        counter = num1 = num2 = 0;
        for (int i = start, j = 0; i < end; i++) {
            if (counter == 0) { 
                num1 = nums[i]; 
            } else {
                num2 = nums[i];
            }
            if (counter == 1) {
                res[j] = (short)(num1 * num2);
                //double a = Math.sqrt(num1 + i) * Math.sin(num2 + i);
                //double b = Math.sqrt(num1 + i) * Math.sin(num2 + i);
                j++;
                counter = 0;
                continue;
            }
            counter++;
        }
        if (Main.SHOW_INFO) {
            String str = "";
            for (int i = 0; i < res.length; i++) {
                str += res[i] + " ";
            }
            System.out.println(Thread.currentThread().getName() + " result: " + str);
        }
        return res;
    }
}




