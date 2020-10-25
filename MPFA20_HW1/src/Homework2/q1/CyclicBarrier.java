package Homework2.q1;
import java.util.concurrent.Semaphore;

public class CyclicBarrier {
	//counting semaphore 
	private Semaphore sem_allParties;
	private Semaphore sem_arrivalIdx;
	private int arrivalIdx;
	private final static Object obj = new Object();
	//private boolean waiting = true;
    public CyclicBarrier(int parties) {
    	// Creates a new CyclicBarrier that will release threads only when
    	// the given number of threads are waiting upon it    	
    	//check parties input
    	if (parties <= 0) {
    		//throw an exception
    	}
    	this.arrivalIdx = parties;
        this.sem_allParties = new Semaphore(parties-1, true);
        this.sem_arrivalIdx = new Semaphore(1, true);	
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
    	sem_arrivalIdx.acquire();
    	arrivalIdx--;
    	retval = arrivalIdx;
    	sem_arrivalIdx.release();
    	
    	if(sem_allParties.tryAcquire()) {
    		//this.waiting = true;
    		synchronized(obj) {
    			obj.wait();
    		}
    		sem_allParties.release();
    	}
    	else {
    		synchronized(obj) {
    			obj.notifyAll();
    		}
    	}
        
        return retval;
    }
}

