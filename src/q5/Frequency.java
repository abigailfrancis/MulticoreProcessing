package q5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Frequency implements Callable<Integer> {

    private final int target;
    private final int[] subArray;

    /*
    Creates a new instance of the Frequency class
     */
    public Frequency(int target, int[] subArray) {
        this.target = target;
        this.subArray = subArray;
    }

    @Override
    public Integer call() {
        Integer total = 0;
        for (int i = 0; i < this.subArray.length; i++) {
            if (this.subArray[i] == this.target) {
                total++;
            }
        }
        return total;
    }

    /*
    Creates numThreads threads that will compute the frequency of integer x within Array A
     */
    public static int parallelFreq(int x, int[] A, int numThreads) {
        // Input validation
        // Check if numThreads is an invalid number
        if (numThreads <= 0) {
            return -1;
        }

        // Check if array is null or empty
        if (A == null || A.length == 0) {
            return 0;
        }

        // Check if we're being asked to generate more threads than the size of the array
        if (numThreads > A.length) {
            // If we've been asked to divide the array into too many pieces
            numThreads = A.length;
        }

        int interval = A.length / numThreads;

        // Create ExecutorService with numThreads threads
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Create a list of Frequency callables
        List<Frequency> callables = new ArrayList<>();

        int start;
        int end;
        int[] subArr;
        for (int i = 0; i < numThreads; i++) {
            start = i * interval;
            end = (i == numThreads - 1) ? A.length : start + interval;

            subArr = Arrays.copyOfRange(A, start, end);

            Frequency f = new Frequency(x, subArr);
            callables.add(f);
        }

        // Create a list of Futures to hold the results of the Callables
        List<Future<Integer>> futures;

        // Create an integer to track the result frequency
        int result = 0;

        // Invoke all of the Callables
        try {
            futures = executor.invokeAll(callables);

            // Process the results
            for (Future<Integer> fut : futures) {
                result += fut.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Shut down the executor service
        executor.shutdown();

        // Return the total computed frequency
        return result;
    }
}
