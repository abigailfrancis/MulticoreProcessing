package Homework3.q3a;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class QueueTests {

    private static MyQueue q;
    private static List<Integer> listOfItemsToEnqueue;
    public QueueTests(MyQueue q)
    {
        this.q = q;
    }

    @Parameterized.Parameters()
    public static Collection data() {
        ArrayList<Object> testParams = new ArrayList<Object>();

        // Run with both types of MyQueue
        testParams.add(new LockQueue());
        //testParams.add(new LockFreeQueue());

        return testParams;
    }

    @Test
    public void TestLockQueue_Simple() {
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
  //  @Test
    public void TestLockFreeQueue_large() throws Exception
    {
        // Arrange
    	listOfItemsToEnqueue =  Arrays.asList();
        // Set up the list of items to enqueue
    	List<Integer> newList = new ArrayList<Integer>();
        for(int i = 0; i < 100; i++) {
        	newList.add(i);
        }
        listOfItemsToEnqueue = newList;
        // Set up variables to track dequeued results
        HashSet<Integer> dequeuedItems = new HashSet<Integer>();
        AtomicInteger numDequeuedNulls = new AtomicInteger(0);

        // Define the number of producers & consumers
        int numProducers = 100;
        int numConsumers = 100;

        // Act
        testHelper2(numProducers, numConsumers, numDequeuedNulls, dequeuedItems);
        System.out.println("Size of Dequ: " + dequeuedItems.size());
        // Assert
        
        assertEquals(0, numDequeuedNulls.get());
        for(int i = 0; i<100; i++) {
        	assertTrue(dequeuedItems.contains(i));
        }
    }
    //@Test
    public void TestLockFreeQueue_Bombard() throws Exception
    {
        // Arrange
    	listOfItemsToEnqueue =  Arrays.asList();
        // Set up the list of items to enqueue
    	List<Integer> newList = new ArrayList<Integer>();
        for(int i = 0; i < 50; i++) {
        	newList.add(i);
        }
        listOfItemsToEnqueue = newList;
        // Set up variables to track dequeued results
        HashSet<Integer> dequeuedItems = new HashSet<Integer>();
        AtomicInteger numDequeuedNulls = new AtomicInteger(0);

        // Define the number of producers & consumers
        int numProducers = 50;
        int numConsumers = 50;

        // Act
        testHelper3(numProducers, numConsumers, numDequeuedNulls, dequeuedItems);
        System.out.println("Size of Dequ: " + dequeuedItems.size());
        // Assert
        System.out.println("Size of nulls: " + numDequeuedNulls.get());
        int countDequeued = 0;
     //   assertEquals(0, numDequeuedNulls.get());
        for(int i = 0; i<50-numDequeuedNulls.get(); i++) {
        	if(dequeuedItems.contains(i)) {
        		countDequeued++;
        	}
        }
        assertTrue((countDequeued + numDequeuedNulls.get() == 50));
    }
    
    public static Boolean enqueue(Integer num) {
        return q.enq(num);
    }

    public static Integer dequeue() {
        return q.deq();	
    }

    private void testHelper(int numProducers, int numConsumers, AtomicInteger numDequeuedNulls, HashSet<Integer> dequeuedItems) {
        // Create numThread threads
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Create list to hold future integer counts
        List<Future<Boolean>> listP = new ArrayList<Future<Boolean>>();
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
            listP = executor.invokeAll(P);

            // Thread.sleep, to allow the producers to finish before the consumers start
            Thread.sleep(1000);

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

            for (Future<Boolean> future : listP) {
                try {
                    var result = future.get();
                    // System.out.println("Something was produced");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (InterruptedException e)
        {
            // Do nothing
        }

        executor.shutdown();
    }
    
    private void testHelper2(int numProducers, int numConsumers, AtomicInteger numDequeuedNulls, HashSet<Integer> dequeuedItems) {
        // Create numThread threads
        ExecutorService executor = Executors.newFixedThreadPool(200);

        // Create list to hold future integer counts
        List<Future<Boolean>> listP = new ArrayList<Future<Boolean>>();
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
            listP = executor.invokeAll(P);

            // Thread.sleep, to allow the producers to finish before the consumers start
            Thread.sleep(10000);

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

            for (Future<Boolean> future : listP) {
                try {
                    var result = future.get();
                    // System.out.println("Something was produced");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (InterruptedException e)
        {
            // Do nothing
        }

        executor.shutdown();
    }
    /*No sleep, execute all at once*/
  
    private void testHelper3(int numProducers, int numConsumers, AtomicInteger numDequeuedNulls, HashSet<Integer> dequeuedItems) {
        // Create numThread threads
        ExecutorService executor = Executors.newFixedThreadPool(100);

        // Create list to hold future integer counts
        List<Future<Boolean>> listP = new ArrayList<Future<Boolean>>();
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
            listP = executor.invokeAll(P);
            // Thread.sleep, to allow the producers to finish before the consumers start
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

            for (Future<Boolean> future : listP) {
                try {
                    var result = future.get();
                    // System.out.println("Something was produced");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (InterruptedException e)
        {
            // Do nothing
        }

        executor.shutdown();
    }
    class Producer implements Callable<Boolean> {
        @Override
        public Boolean call(){
            var result = true;
            for (int p = 0; p < listOfItemsToEnqueue.size(); p++) {
                result = result & QueueTests.enqueue(listOfItemsToEnqueue.get(p));
            }
            return result;
        }
    }


    class Consumer implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            return QueueTests.dequeue();
        }
    }
}