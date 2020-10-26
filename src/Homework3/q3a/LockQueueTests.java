package Homework3.q3a;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LockQueueTests {
    /*
    Unit tests for Lock Queue
     */
    private static LockQueue q;

    public LockQueueTests() {
        q = new LockQueue();
    }

    // Todo: Tests with one P, 2Cs... 2Ps one C? etc.

    @Test
    public void TestLockQueue_Simple() {
        LockQueue q = new LockQueue();
        assertTrue(q.deq() == null);
        q.enq(5);
        q.enq(30);
        assertTrue(q.deq() == 5);
        assertTrue(q.deq() == 30);
        assertTrue(q.deq() == null);
    }

    @Test
    public void TestLockQueue_Simple2() throws InterruptedException {
        LockQueueEnqueueOp enqOp = new LockQueueEnqueueOp();
        LockQueueDequeueOp deqOp = new LockQueueDequeueOp();

        HashSet<Integer> dequeuedItems = new HashSet<Integer>();
        List<Integer> enqueuedItems = Arrays.asList(1, 2, 3, 4, 5);

        Thread t1 = new Thread(enqOp);
        t1.start();
        Thread t2 = deqOp.call();
        t2.start();
        t1.join();
        t2.join();
    }

    public static void enqueue()
    {
        q.enq(5);
    }

    public static Integer dequeue()
    {
        return q.deq();
    }

}

class LockQueueEnqueueOp implements Runnable{
    public void run(){
        LockQueueTests.enqueue();
    }
}

class LockQueueDequeueOp implements Callable {
    @Override
    public Object call() throws Exception {
        return LockQueueTests.dequeue();
    }
}