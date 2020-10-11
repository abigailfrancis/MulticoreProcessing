package Homework2.q1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class CyclicBarrierTester implements Runnable {

    public static CyclicBarrier cyclicBarrier;
    public final int numParties;
    public final int threadNum;

    public static HashMap<Integer, Integer> expectedArrivalIndices;
    private static Object lock;

    public CyclicBarrierTester(int numParties, int threadNum)
    {
        this.numParties = numParties;
        this.threadNum = threadNum;

        this.lock = new ReentrantLock();
    }

    public static void runTest(int numParties, int numThreads)
    {
        cyclicBarrier = new CyclicBarrier(numParties);
        expectedArrivalIndices = new HashMap<Integer, Integer>();

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            futures.add(executorService.submit(new CyclicBarrierTester(numParties, i)));
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
    }

    @Override
    public void run() {

        // System.out.println(Thread.currentThread().getName() + " is waiting for all other threads to reach common barrier point");
        int myArrivalIndex = -1;
        try
        {
            myArrivalIndex = cyclicBarrier.await();

            synchronized (lock) {
                if (expectedArrivalIndices.containsKey(myArrivalIndex)) {
                    expectedArrivalIndices.replace(myArrivalIndex, expectedArrivalIndices.get(myArrivalIndex) + 1);
                } else {
                    expectedArrivalIndices.put(myArrivalIndex, 1);
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // System.out.println("Arrival index: " + myArrivalIndex);
    }
}
