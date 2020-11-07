package Homework3.q3a;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue implements MyQueue {
    // you are free to add members
	public AtomicInteger count;
	private ReentrantLock enqLock, deqLock;
	Node head, tail;
    public LockQueue() {
        // implement your constructor here
    	count = new AtomicInteger();
    	count.getAndSet(0);
    	enqLock = new ReentrantLock();
    	deqLock = new ReentrantLock();
    	head = new Node(null);
    	tail = head;
    }

    public boolean enq(Integer value) {
        // implement your enq method here
    	if(value == null) throw new NullPointerException();
    	enqLock.lock();
    	try {
    		Node enq_node = new Node(value);
    		tail.next = enq_node;
    		tail = enq_node;
    		count.getAndIncrement();
    	} finally {
    		enqLock.unlock();
    	}
        return true;
    }

    public Integer deq() {
        // implement your deq method here
    	int result = -1;
    	deqLock.lock();
    	try {
    		if(head.next == null) {
    			return null;
    		}
    		result = head.next.value;
    		head = head.next;
    		count.getAndDecrement();
    	} finally {
    		deqLock.unlock();
    	}
        return result;
    }
    
    public Integer getCount() {
    	return count.get();
    }
    protected class Node {
        public Integer value;
        public Node next;

        public Node(Integer x) {
            value = x;
            next = null;
        }
    }
}
