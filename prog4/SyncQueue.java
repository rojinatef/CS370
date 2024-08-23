
public class SyncQueue{

// 1) maintains an array of QueueNode objects, each representing a different 
// condition and enqueuing all threads that wait for this condition 

private QueueNode[] queue;

// 2) are constructors that create a queue and allow threads to wait for a default condition number
// (=10) or a condMax number of condition/event types.

public SyncQueue(){
  // default condition number
     this(10);
}

public SyncQueue(int condMax){

    // create a queue 
    queue = new QueueNode [condMax];
    for (int i=0;i<condMax;i++){
        // initialize each queueNodes
        queue[i]=new QueueNode();
    }
}

// 3) enqueues the calling thread into the queue and sleeps it until a given condition is satisfied. It
//returns the ID of a child thread that has woken the
// calling thread.

    public int enqueueAndSleep(int condition){
      
        return queue[condition].sleep();
    }

// 4) dequeues and wakes up a thread waiting for a given condition. If there are two or more threads
// waiting for the same condition, only one thread is dequeued and resumed. The FCFS (first-come-
// first-service) order does not matter.

public void dequeueAndWakeup(int condition){
    // tid = 0
    dequeueAndWakeup(condition, 0);
}

// 5)  This function can receive the calling thread's ID, (tid) as the 2nd
//argument. This tid will be passed to the thread that has been woken up from enqueueAndSleep. If no
//2nd argument is given, you may regard tid as 0

public void dequeueAndWakeup(int condition, int tid){
   
     queue[condition].wake(tid);
        
    }
  



} 
