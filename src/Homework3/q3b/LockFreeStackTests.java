package Homework3.q3b;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LockFreeStackTests {

    private static MyStack stack;
    private static List<Integer> listOfItemsToPush;

    public LockFreeStackTests()
    {
        stack = new LockFreeStack();
    }

    @Test
    public void TestLockFreeStack_Simple() throws EmptyStack {
        //assertThrows(EmptyStack.class, () -> stack.pop());
        stack.push(5);
        stack.push(10);
        stack.push(15);
        assertTrue(stack.pop() == 15);
        assertTrue(stack.pop() == 10);
        assertTrue(stack.pop() == 5);
        assertThrows(EmptyStack.class, () -> stack.pop());
    }

    @Test
    public void TestLockFreeStack_Simple2() throws EmptyStack {
        LockFreeStack q = new LockFreeStack();
        assertThrows(EmptyStack.class, () -> stack.pop());
        stack.push(5);
        assertTrue(stack.pop() == 5);
        assertThrows(EmptyStack.class, () -> stack.pop());
        stack.push(10);
        stack.push(15);
        stack.push(20);
        assertTrue(stack.pop() == 20);
        assertTrue(stack.pop() == 15);
        assertTrue(stack.pop() == 10);
        assertThrows(EmptyStack.class, () -> stack.pop());
    }

    @Test
    public void TestLockFreeStack_OneP_OneC()
    {
        // Arrange
        // Set up the list of items to push
        listOfItemsToPush =  Arrays.asList(1);

        // Set up variables to track popped results
        HashSet<Integer> poppedItems = new HashSet<Integer>();
        AtomicInteger numEmptyStackExceptions = new AtomicInteger(0);

        // Define the number of producers and consumers
        int numProducers = 1;
        int numConsumers = 1;

        // Act
        stackTestHelper(numProducers, numConsumers, numEmptyStackExceptions, poppedItems);

        // Assert
        assertEquals(0, numEmptyStackExceptions.get());
        assertTrue(poppedItems.contains(1));
    }

    @Test
    public void TestLockFreeStack_OneP_FourC()
    {
        // Arrange
        // Set up the list of items to push
        listOfItemsToPush =  Arrays.asList(1, 2, 3);

        // Set up variables to track popped results
        HashSet<Integer> poppedItems = new HashSet<Integer>();
        AtomicInteger numEmptyStackExceptions = new AtomicInteger(0);

        // Define the number of producers & consumers
        int numProducers = 1;
        int numConsumers = 4;

        // Act
        stackTestHelper(numProducers, numConsumers, numEmptyStackExceptions, poppedItems);

        // Assert
        assertEquals(1, numEmptyStackExceptions.get());
        assertTrue(poppedItems.contains(1));
        assertTrue(poppedItems.contains(2));
        assertTrue(poppedItems.contains(3));
    }

    public static Boolean push(Integer num) {
        return stack.push(num);
    }

    public static Integer pop() throws EmptyStack{
        return stack.pop();
    }

    private void stackTestHelper(int numProducers, int numConsumers, AtomicInteger numEmptyStackExceptions, HashSet<Integer> poppedItems) {
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
            Thread.sleep(100);

            listC = executor.invokeAll(C);

            for (Future<Integer> future : listC) {
                try {
                    var result = future.get();
                    poppedItems.add(result);
                }
                catch (Exception e)
                {
                    if (e.getCause() instanceof EmptyStack)
                    {
                        numEmptyStackExceptions.getAndIncrement();
                    }
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
            for (int p = 0; p < listOfItemsToPush.size(); p++) {
                result = result & LockFreeStackTests.push(listOfItemsToPush.get(p));
            }
            return result;
        }
    }

    class Consumer implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            return LockFreeStackTests.pop();
        }
    }
}