package Homework3.q3a;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue implements MyQueue {
    private static ReentrantLock enqLock;
    private static ReentrantLock deqLock;
    Node head;
    Node tail;
    private static AtomicInteger count;

    public LockQueue() {
        head = new Node(-1);
        tail = head;
        enqLock = new ReentrantLock();
        deqLock = new ReentrantLock();
        count = new AtomicInteger(0);
    }

    public boolean enq(Integer value) {
        var enqSucceeded = false;
        enqLock.lock();

        try {
            Node newNode = new Node(value);
            tail.next = newNode;
            tail = newNode;
            count.getAndIncrement();
            enqSucceeded = true;
        }
        finally {
            enqLock.unlock();
            return enqSucceeded;
        }
    }

    public Integer deq() {
        Integer result;
        deqLock.lock();

        if (head.next == null)
        {
            deqLock.unlock();
            return null;
        }

        result = head.next.value;
        head = head.next;
        count.getAndDecrement();

        deqLock.unlock();

        return result;
    }

    protected class Node {
        public Integer value;
        public Node next;

        public Node(Integer x) {
            value = x;
            next = null;
        }
    }

    public AtomicInteger getCount()
    {
        return count;
    }
}
