package q5;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Frequency implements Callable<Integer>{
	int x;
	int [] A;
	int numThreads;
	int blksize;
	int index;
	int startIdx;
	
	public Frequency(int x, int[] A, int index, int numThreads, int blksize, int startIdx) {
        this.x = x;
        this.A = A;
        this.numThreads = numThreads;
        this.blksize = blksize;
        this.index = index;
        this.startIdx = startIdx;
    }
	public Integer call() {
		//calculate num x in sub array
		int count = 0;
		int startIdx = this.startIdx;
		int endIdx = this.startIdx + this.blksize;
		
		if (endIdx > this.A.length) {
			endIdx = this.A.length;
		}
		for(int i = startIdx; i < endIdx; i++) {
			if (this.x == this.A[i]) {
				count += 1;
			}
		}
		
		return count;
	}
	public static int parallelFreq (int x, int[] A, int numThreads) {
		int result = 0;
		int remainder = 0;
		int startIdx = 0;
		//check input validity
		if (A.length == 0 | numThreads == 0) {
			return 0;
		}
		//if the number of threads is greater than the size, limit 1 array value per thread
		if(numThreads > A.length) {
			numThreads = A.length;
		}
		else {
			remainder = A.length % numThreads;
		}
		//create numThread threads
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		
		//create list to hold future integer counts
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		
		//create callable instance
		List<Frequency> F = new ArrayList<>();
		
		for(int i = 0; i < numThreads; i++) {
			if(remainder > 0) {
				Frequency f = new Frequency(x, A, i, numThreads, (int)Math.ceil((A.length/numThreads))+1, startIdx); //create new callable
				startIdx = startIdx + (int)Math.ceil((A.length/numThreads))+1;
				remainder -= 1;
				F.add(f); //add new callable to the list
			}
			else {
				Frequency f = new Frequency(x, A, i, numThreads, (int)Math.ceil((A.length/numThreads)), startIdx); //create new callable
				startIdx = startIdx + (int)Math.ceil((A.length/numThreads));
				F.add(f); //add new callable to the list
			}
				
			
		}
		
		//Future<integer> future = executor.submit(callable);
		try {
			list = executor.invokeAll(F);
			
			for(Future<Integer> future: list) {
				try {
					result += future.get();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}		
		 
		} catch(InterruptedException e) {};
		
		executor.shutdown();
		
		return result;
	}
	
/*	public static void main(String[] args){
		int x = 2;//args[0]; //input search
		int [] A = {1,2,3,4,1,2,3,4,4,4,4,4,4,4,4,4}; //input array
		numThreads = 4;
		parallelFreq f = new parallelFreq(x,A,numThreads);
		f.start()
		try {
			f.join();
		}catch(InterruptedException e) {};
		System.out.println("OUTPUT: " + f.get());
	}*/
}