package q6.Tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


import q6.Tournament.PIncrement;

//import q6.PIncrement;

public class PIncrement implements Callable<Integer>{
	private static final int m = 1200000;
	static private int count;
	private static int[] turn;
    private static int[] flag;
	int ID;
	int inc;
	int n;
	public PIncrement(int ID, int inc, int numThreads) {
        this.ID = ID;
        this.inc = inc;
        this.n = numThreads;
        flag[ID] = 0;
     }
	public Integer call() {
		for(int i = 0; i < inc; i++) {
			increment();
		}
		//this isn't using the method indicated by the assignment
		return inc;
	}
	private void increment() {
		lock();
		count++;
		unlock();
        
    }
    private void lock() {
    	int gate = 0;
    	boolean allgates = true;
        for(int k = 1; k < n; k++) {
        		flag[ID] = k;
        		turn[k] = ID;  
        		while(turn[k] == ID || allgates) {
        			if(searchFlag(k, gate)) {
        				gate++;
        			}
        			if(gate == n) {
        				allgates = false;
        			}
        		}
        				
        }
    }
    private boolean searchFlag(int k, int gate) {
    	boolean isLTk = true;
    		if(gate != ID) {
    			isLTk = (flag[gate] < k);
    		}
    	return isLTk;
    }
    private void unlock() {
        flag[ID] = 0; // not interested in the critical section
    }
	public static int parallelIncrement(int c, int numThreads) {
	// your implementation goes here
		int inc = m/numThreads;
		count = c;
		flag = new int[numThreads];
		turn = new int[numThreads];
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
			I_list.add(new PIncrement(i, inc, numThreads));
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
	    System.out.println("Peterson's Tournament [" + numThreads + ", " + c + "]: " + durationInMs + " ms");
		executor.shutdown();
		return c;
	}

}
