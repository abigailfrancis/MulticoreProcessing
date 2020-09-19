package q6.ReentrantLock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import q6.ReentrantLock.PIncrement;

//import q6.PIncrement;

public class PIncrement implements Callable<Integer>{
	private static final int m = 1200000;
	static private int count;
	private static Lock lock = new ReentrantLock();
	int ID;
	int inc;
	
	public PIncrement(int ID, int inc) {
        this.ID = ID;
        this.inc = inc;
     }
	public Integer call() {
		for(int i = 0; i < inc; i++) {
			increment();
		}
		//this isn't using the method indicated by the assignment
		return inc;
	}
	private void increment() {
		try {
			if(lock.tryLock(10, TimeUnit.SECONDS)){
				count++;
				}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
        
    }
	public static int parallelIncrement(int c, int numThreads) {
	// your implementation goes here
		int inc = m/numThreads;
		count = c;
		//create numThread threads
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		//create list to hold future integer counts
		List<Future<Integer>> ret_list = new ArrayList<Future<Integer>>();
				
		//create callable instance
		List<PIncrement>  I_list= new ArrayList<>();
		
		for(int i = 0; i < numThreads; i++) {
			//if numThreads doesn't divide equally into m, allow last thread to supplement
			if(i == numThreads - 1) {
				inc += m - numThreads*(m/numThreads);
			}
			//add each new thread to the list
			I_list.add(new PIncrement(i, inc));
		}
		
	    // Start the timer
	    long start = System.nanoTime();
	    
	    //execute 
		try {
			ret_list = executor.invokeAll(I_list);
		} catch(InterruptedException e) {};
		c = count;
		//TIMING COMPUTATION 
		// End the timer
	    long end = System.nanoTime();
	    // Compute the duration
	    long duration = end - start;
	    double durationInMs = duration / 1000000.0;
	    System.out.println("ReentrantLock [" + numThreads + ", " + c + "]: " + durationInMs + " ms");
		executor.shutdown();
		return c;
	}

}

