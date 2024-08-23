import java.util.*;

/*

Write a user-level test thread called Test3.java which spawns and waits for
the completion of X pairs of threads (where X = 2 ~ 6 or more), one conducting
only numerical computation and the other reading/writing many blocks
randomly across the disk

*/



public class Test3 extends Thread {
    private int loop;

    public Test3(String[] args) {
        if (args.length == 0) {
            SysLib.cout("no argument, try again!!");
        } else {
            loop = Integer.parseInt(args[0]);
        }
    }

    public void run() {
        // start timing
        long start = System.currentTimeMillis(); 

        for (int i = 0; i < loop; i++) {
            // numerical computation
            SysLib.exec(new String[]{"TestThread3", "computation"});
            SysLib.cout("it is computing!!\n");
            // reading and writing on disk
            SysLib.exec(new String[]{"TestThread3", "disk"});
            SysLib.cout("it is reading and writing on the disk!!\n");
        }

        // Wait for all threads to complete
        // we have 2 execs per loop, so we need to wait for loop * 2 completions
        
        for (int i = 0; i<2*loop; i++) { 
            SysLib.join();
        }
        // stop timing 
        long finish = System.currentTimeMillis(); 
        long elapsed_time = finish - start;
        // this format according to this --> elapsed time = 85162 msec
        SysLib.cout("elapsed time is = " + elapsed_time + " msec\n");
        SysLib.exit();
    }
}
