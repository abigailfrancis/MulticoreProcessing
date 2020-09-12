package q6.ReentrantLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class PIncrement implements Runnable{

    private static int counter;
    private static int[] totalIncrementOperations;

    private final int threadNum;
    private ReentrantLock lock;

    /*
     * Creates a new instance of the PIncrement class
     */
    public PIncrement(int threadNum, ReentrantLock lock) {
        this.threadNum = threadNum;
        this.lock = lock;
    }

    /*
      Initiates numThreads threads and uses them to perform 1,200,000 increments to shared variable 'c'
      */
    public static int parallelIncrement(int c, int numThreads){

        // Validate input
        if (numThreads <= 0 || numThreads > 8) {
            return -1;
        }

        // Determine number of increment operations required
        totalIncrementOperations = new int[numThreads];
        for(int t = 0; t < numThreads; t++) {
            totalIncrementOperations[t] = 1200000 / (numThreads);
        }

        // Special case because 7 does not divide evenly into 1,200,000
        if(numThreads == 7) {
            totalIncrementOperations[6] = 171432;
        }

        // Initialize the shared lock
        ReentrantLock sharedLock = new ReentrantLock();

        // Initialize the static int counter
        counter = c;

        // Start the timer
        long startTime = System.nanoTime();

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            futures.add(executorService.submit(new PIncrement(i, sharedLock)));
        }
        executorService.shutdown();

        // Wait for the results to become available
        for (int i = 0; i < numThreads; i++) {
            try {
                futures.get(i).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // End the timer
        long endTime = System.nanoTime();

        // Compute the duration
        long duration = endTime - startTime;
        double durationInMs = duration / 1000000.0;
        System.out.println("ReentrantLock: [" + numThreads + ", " + c + "]: " + durationInMs + " ms");

        // Return the final count
        return counter;
    }

    @Override
    public void run() {
        for (int p = 0; p < totalIncrementOperations[this.threadNum]; p++) {
            increaseCounter();
        }
    }

    private void increaseCounter() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }
}
