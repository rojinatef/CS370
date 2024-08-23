import java.util.*;

public class Scheduler extends Thread
{
    //it has three queues, so I craeted 3 queues
	private Vector<TCB> que0, que1,que2;
    private int timeSlice;
    private static final int DEFAULT_TIME_SLICE = 1000;

    // New data added to p161 
    private boolean[] tids; // Indicate which ids have been used
    private static final int DEFAULT_MAX_THREADS = 10000;

    // A new feature added to p161 
    // Allocate an ID array, each element indicating if that id has been used
    private int nextId = 0;
    private void initTid( int maxThreads ) {
	tids = new boolean[maxThreads];
	for ( int i = 0; i < maxThreads; i++ )
	    tids[i] = false;
    }

    // A new feature added to p161 
    // Search an available thread ID and provide a new thread with this ID
    private int getNewTid( ) {
	for ( int i = 0; i < tids.length; i++ ) {
	    int tentative = ( nextId + i ) % tids.length;
	    if ( tids[tentative] == false ) {
		tids[tentative] = true;
		nextId = ( tentative + 1 ) % tids.length;
		return tentative;
	    }
	}
	return -1;
    }

    // A new feature added to p161 
    // Return the thread ID and set the corresponding tids element to be unused
    private boolean returnTid( int tid ) {
	if ( tid >= 0 && tid < tids.length && tids[tid] == true ) {
	    tids[tid] = false;
	    return true;
	}
	return false;
    }

    // A new feature added to p161 
    // Retrieve the current thread's TCB from the queue
    public TCB getMyTcb() {
    Thread myThread = Thread.currentThread(); // Get my thread object
	// synchronize on the current case 
    synchronized(this) { 
        // pervisously beacuse we had only one que it only search on that que. Now we have three so we need to search on each queues
        for (TCB tcb : que0) {
            if (tcb.getThread() == myThread) {
                return tcb;
            }
        }
        // search in que1
        for (TCB tcb : que1) {
            if (tcb.getThread() == myThread) {
                return tcb;
            }
        }
        // search in que2
        for (TCB tcb : que2) {
            if (tcb.getThread() == myThread) {
                return tcb;
            }
        }
    }
    return null; 
}


    // A new feature added to p161 
    // Return the maximal number of threads to be spawned in the system
    public int getMaxThreads( ) {
	return tids.length;
    }

    public Scheduler( ) {
	timeSlice = DEFAULT_TIME_SLICE;
	// instead of one vector now we have 3, so we need to intialize three of them
	que0 = new Vector<>();
	que1 = new Vector<>();
	que2 = new Vector<>();
	initTid( DEFAULT_MAX_THREADS );
    }

    public Scheduler( int quantum ) {
	timeSlice = quantum;
	que0 = new Vector<>();
	que1 = new Vector<>();
	que2 = new Vector<>();
	initTid( DEFAULT_MAX_THREADS );
    }

    // A new feature added to p161 
    // A constructor to receive the max number of threads to be spawned
    public Scheduler( int quantum, int maxThreads ) {
	timeSlice = quantum;
	que0 = new Vector<>();
	que1 = new Vector<>();
	que2 = new Vector<>();
	initTid( maxThreads );
    }
	// I had to add the millisecond beacuse I use schedulerSleep(timeSlice/2)
    private void schedulerSleep(int milliseconds) {
	try {
	    Thread.sleep( timeSlice );
	} catch ( InterruptedException e ) {
	}
    }

    // A modified addThread of p161 example
    public TCB addThread( Thread t ) {
	t.setPriority( 2 );
	TCB parentTcb = getMyTcb( ); // get my TCB and find my TID
	int pid = ( parentTcb != null ) ? parentTcb.getTid( ) : -1;
	int tid = getNewTid( ); // get a new TID
	if ( tid == -1)
	    return null;
	TCB tcb = new TCB( t, tid, pid ); // create a new TCB
	//new thread's TCB is always enqueued into Queue0.
	que0.add( tcb );
	return tcb;
    }

    // A new feature added to p161
    // Removing the TCB of a terminating thread
    public boolean deleteThread( ) {
	TCB tcb = getMyTcb( ); 
	if ( tcb!= null )
	    return tcb.setTerminated( );
	else
	    return false;
    }

    public void sleepThread( int milliseconds ) {
	try {
	   sleep( milliseconds );
	} catch ( InterruptedException e ) { }
    }

	//check to see if new threads is in que0 so reexecute the que0
	private boolean checkQ0(Thread current){
		if (!que0.isEmpty()) {
                current.suspend();
				return true;
            }
            current.resume();
			return false;

	}
    //check for the higher priority queues
    private void check_priority (Thread current){
        // execute in increments to check higher priority queues
			int attemp = 0;
			while (attemp <= 3) {
    		schedulerSleep(timeSlice/2);
    		if (!que0.isEmpty() || !que1.isEmpty()) {
        	current.suspend(); 
        	return;
    	}
   			 attemp++;
	}
            
		current.resume();
    }
        
 public void run() {

    while (true) {
	// first I check if all queues are empty
		if (que0.isEmpty() && que1.isEmpty() && que2.isEmpty()) {
			System.out.println("all queues are empty, scheduler waiting!");
		}
        // then, first we need to execute all threads in que0 
		// I used the previous codes for que and change it according to three queues and their priority. 
        while (!que0.isEmpty()) {
            TCB currentTCB = (TCB) que0.firstElement();
            if (currentTCB.getTerminated()==true) {
                que0.remove(currentTCB);
                returnTid(currentTCB.getTid());
                continue;
            }
            Thread current = currentTCB.getThread();
            if (current != null) {
                if (current.isAlive()) {
                    current.resume();
                } else {
                    current.start();
                }
            } 
			//  Queue0 whose time quantum Q0 is half of the time quantum which is 500
            schedulerSleep(timeSlice/2);
            synchronized (que0) {
                if (current != null && current.isAlive())
                    current.suspend();
                que0.remove(currentTCB);
				// if a thread in the que0 does not complete its execution, then scheduler moves the corresponding TCB
				//to que1
                que1.add(currentTCB);
            }
        }

        // then it executes threads in Que1 if Que0 is empty
        while (!que1.isEmpty() && que0.isEmpty()) {
            TCB currentTCB = (TCB) que1.firstElement();
            if (currentTCB.getTerminated()==true) {
                que1.remove(currentTCB);
                returnTid(currentTCB.getTid());
                continue;
            }
            Thread current = currentTCB.getThread();
            if (current != null) {
                if (current.isAlive()) {
                    current.resume();
                } else {
                    current.start();
                }
            } 

			// for checking que0 we need to use half quantum
            schedulerSleep(timeSlice/2);
		// if q0 has tasks break and finish that first 
            if (checkQ0(current)){
				break;
			}
            
			// complete the quantum
            schedulerSleep(timeSlice/2);
		// if a thread in the que1 does not complete its execution, then scheduler moves the corresponding TCB
				//to que2
            synchronized (que1) {
                if (current != null && current.isAlive())
                    current.suspend();
                que1.remove(currentTCB);
                que2.add(currentTCB);
            }
        }

        // then execute threads in que2 if que0 and que1 are empty
        while (!que2.isEmpty() && que0.isEmpty() && que1.isEmpty()) {
            TCB currentTCB = (TCB) que2.firstElement();
            if (currentTCB.getTerminated()==true) {
                que2.remove(currentTCB);
                returnTid(currentTCB.getTid());
                continue;
            }
            //check the priority
            Thread current = currentTCB.getThread();
            if (current != null) {
                if (current.isAlive()) {
                    current.resume();
                } else {
                    current.start();
                }
            } 
			//check higher priority queues
			check_priority(current);

		synchronized (que2) {
			if (current != null && current.isAlive())
				current.suspend();
			que2.remove(currentTCB);
			que2.add(currentTCB);
		}
	}

        try {
			// when all queues are empty
            Thread.sleep(10); 
        } catch (InterruptedException e3) {
            
        }
    }
}


	
    
}