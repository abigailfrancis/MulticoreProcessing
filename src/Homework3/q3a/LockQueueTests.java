package Homework3.q3a;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LockQueueTests {

    private static MyQueue q;
    private static Random generator;
    private static List<Integer> listOfItemsToEnqueue;

    public LockQueueTests() {

        // Run these tests with either Queue implementation
        // q = new LockFreeQueue();
        q = new LockQueue();

        generator = new Random();
    }

    @Test
    public void TestLockQueue_Simple() {
        LockQueue q = new LockQueue();
        assertTrue(q.deq() == null);
        q.enq(5);
        q.enq(10);
        q.enq(15);
        assertTrue(q.deq() == 5);
        assertTrue(q.deq() == 10);
        assertTrue(q.deq() == 15);
        assertTrue(q.deq() == null);
    }

    @Test
    public void TestLockQueue_Simple2() {
        LockQueue q = new LockQueue();
        assertTrue(q.deq() == null);
        q.enq(5);
        assertTrue(q.deq() == 5);
        q.enq(10);
        q.enq(15);
        assertTrue(q.deq() == 10);
        assertTrue(q.deq() == 15);
        assertTrue(q.deq() == null);
    }

    @Test
    public void TestLockQueue_OneP_OneC() throws Exception
    {
        // Arrange
        // Set up the list of items to enqueue
        listOfItemsToEnqueue =  Arrays.asList(1);

        // Set up variables to track dequeued results
        HashSet<Integer> dequeuedItems = new HashSet<Integer>();
        AtomicInteger numDequeuedNulls = new AtomicInteger(0);

        // Define the number of producers and consumers
        int numProducers = 1;
        int numConsumers = 1;

        // Act
        testHelper(numProducers, numConsumers, numDequeuedNulls, dequeuedItems);

        // Assert
        assertEquals(0, numDequeuedNulls.get());
        assertTrue(dequeuedItems.contains(1));
    }

    @Test
    public void TestLockQueue_OneP_FourC() throws Exception
    {
        // Arrange
        // Set up the list of items to enqueue
        listOfItemsToEnqueue =  Arrays.asList(1, 2, 3);

        // Set up variables to track dequeued results
        HashSet<Integer> dequeuedItems = new HashSet<Integer>();
        AtomicInteger numDequeuedNulls = new AtomicInteger(0);

        // Define the number of producers & consumers
        int numProducers = 1;
        int numConsumers = 4;

        // Act
        testHelper(numProducers, numConsumers, numDequeuedNulls, dequeuedItems);

        // Assert
        assertEquals(1, numDequeuedNulls.get());
        assertTrue(dequeuedItems.contains(1));
        assertTrue(dequeuedItems.contains(2));
        assertTrue(dequeuedItems.contains(3));
    }

    public static void enqueue(Integer num) {
            q.enq(num);
    }

    public static Integer dequeue() {
        return q.deq();
    }

    private void testHelper(int numProducers, int numConsumers, AtomicInteger numDequeuedNulls, HashSet<Integer> dequeuedItems) {
        // Create numThread threads
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Create list to hold future integer counts
        List<Future> listP = new ArrayList<Future>();
        List<Future<Integer>> listC = new ArrayList<Future<Integer>>();

        List<Producer> P = new ArrayList<>();
        List<Consumer> C = new ArrayList<>();

        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer();
            C.add(consumer);
        }

        for (int i = 0; i < numProducers; i++) {
            Producer producer = new Producer();
            P.add(producer);
        }

        try {
            for(int i = 0; i < P.size(); i++)
            {
                listP.add(executor.submit(P.get(i)));
            }

            // Thread.sleep, to allow the producers to finish before the consumers start
            Thread.sleep(100);

            listC = executor.invokeAll(C);

            for (Future<Integer> future : listC) {
                try {
                    var result = future.get();
                    if (result == null) {
                        // System.out.println("C: null");
                        numDequeuedNulls.getAndIncrement();
                    }
                    else {
                        // System.out.println("C:" + result);
                        dequeuedItems.add(result);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            for (Future<Integer> future : listP) {
                future.get();
                // System.out.println("Something was produced");
            }

        }
        catch (InterruptedException e)
        {
            // Do nothing
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }

    class Producer implements Runnable{

        @Override
        public void run() {
            for (int p = 0; p < listOfItemsToEnqueue.size(); p++) {
                LockQueueTests.enqueue(listOfItemsToEnqueue.get(p));
            }
        }
    }


    class Consumer implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            return LockQueueTests.dequeue();
        }
    }
}



