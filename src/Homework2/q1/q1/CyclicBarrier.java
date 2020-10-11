package Homework2.q1;
import java.util.concurrent.Semaphore;

public class CyclicBarrier {
	//counting semaphore 
	private static Semaphore sem_allParties;
	private static Semaphore sem_arrivalIdx;
	private static Semaphore sem_round;
	private int arrivalIdx;
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
        this.sem_arrivalIdx = new Semaphore(1, true);
        this.sem_round = new Semaphore(1,true);
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
    	if(sem_round.availablePermits()==0) {
    		System.out.println("Here1");
    		while(sem_round.availablePermits()==0);
    	}

    	sem_arrivalIdx.acquire();
    	arrivalIdx--;
    	System.out.println(arrivalIdx);
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
    			sem_round.acquire();
    			obj.notifyAll();
    			arrivalIdx = this.parties;
    			System.out.println("ROUND DONE");
    			sem_round.release();
    		}
    	}
        
        return retval;
    }
}

