package Homework2.q1;
import java.util.concurrent.Semaphore;

public class CyclicBarrier {
	//counting semaphore 
	private static Semaphore sem_waitingParties;
	private static Semaphore sem_allParties;
	private static Semaphore sem_arrivalIdx;
	private static Semaphore sem_barrier = new Semaphore(0);
	private static int arrivalIdx;
	private int parties;
	private final static Object obj = new Object();
	//private boolean waiting = true;
    public CyclicBarrier(int parties) {
    	// Creates a new CyclicBarrier that will release threads only when
    	// the given number of threads are waiting upon it    	
    	//check parties input
    	if (parties <= 0) {
    		//throw an exception
    	}
    	this.parties = parties;
    	this.arrivalIdx = parties;
        this.sem_allParties = new Semaphore(parties-1, true);
        this.sem_waitingParties = new Semaphore(parties, true);
        this.sem_arrivalIdx = new Semaphore(1, true);
        //this.sem_round = new Semaphore(1,true);
    }

    int await() throws InterruptedException {
        // Waits until all parties have invoked await on this CyclicBarrier.
    	// If the current thread is not the last to arrive then it is
        // disabled for thread scheduling purposes and lies dormant until
        // the last thread arrives.
        // Returns: the arrival index of the current thread, where index
        // (parties - 1) indicates the first to arrive and zero indicates
        // the last to arrive.
    	int retval = -1;
    	//all threads wait here unless part of the current party
    	sem_waitingParties.acquire();
    	
    	//decrement arrival index
    	sem_arrivalIdx.acquire();
    	arrivalIdx--;
    	System.out.println(arrivalIdx);
    	retval = arrivalIdx;
    	sem_arrivalIdx.release();
    	
    	//if not the last thread, wait
    	if(sem_allParties.tryAcquire()) {
    		sem_barrier.acquire();
    		//release semaphore after notified
    		sem_allParties.release();
    		
    	}
    	else {
    		synchronized(obj) {
    			
    			//set arrivalidx back to parties
    			sem_arrivalIdx.acquire();
        		arrivalIdx = this.parties;
        		sem_arrivalIdx.release();
        		sem_barrier.release(this.parties-1);
        		//notify everyone to release and continue
    			//obj.notifyAll();
    			
    			
        		//allow next party in
    			sem_waitingParties.release(this.parties);
    		}
    	}
        
        return retval;
    }
}

