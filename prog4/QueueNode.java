import java.util.LinkedList;
import java.util.Queue;
/*
implement the monitors utilized by ThreadOS.
While standard Java monitors are only able to wake up one (using notify) or
all (using notifyall()) sleeping threads, the ThreadOS monitors
(implemented in SynchQueue.java) allow threads to sleep and wake up on
a specific condition. These monitors are used to implement two separate but
key aspects of ThreadOS.

*/



public class QueueNode {
    private Queue<Integer> wake_up = new LinkedList<>();
    private int wait = 0;

    public QueueNode() {
    }

    // a thread can put itself to sleep and wait to be notified.
    public synchronized int sleep() {
        wait++;

        try {
            while (wake_up.isEmpty()) { 
                // wait until being notified
                wait(); 
            }
            // to make a thread wake up
            wait--;
            // return and remove what is woken up 
            return wake_up.poll(); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
           SysLib.cout("problem in sleep function!!");
            return -1; 
        }
    }

    // a thread can be woken-up by another thread 
    public synchronized void wake(int tid) {
        // add the tid, I used offer because if the queue is full that would return false
        wake_up.offer(tid);
        if (wait > 0) {
            // notify if there is any waiting thread  
            notify(); 
        }
    }
}
