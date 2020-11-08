package Homework3.q3a;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue implements MyQueue {
    // you are free to add members
	private AtomicReference<Node> head;
    private AtomicReference<Node> tail;
    
    public LockFreeQueue() {
    	//create head and tail
    	head = new AtomicReference<>();
        tail = new AtomicReference<>();
    	//create initial node in list which is both head and tail
    	Node initNode = new Node(-1);
    	//set head and tail to initial node
    	tail.set(initNode);
    	head.set(initNode);
    }

    public boolean enq(Integer value) {
    	//create new node to enq
    	Node tail_enq = new Node(value);
    	
    	Node tail_cpy; //copy of the current tail
    	Node tail_next;

    	//try continuously to add node until done
    	while(true) {

    		tail_cpy = this.tail.get();
    		tail_next = tail_cpy.next.get();
        	
        	//make sure tail was updated to new tail
        	if(tail_next == null) {
        		//set tail next to enq
	    		if(this.tail.get().next.compareAndSet(tail_next, tail_enq)) {
	    			//Set tail to enq
		        	if(this.tail.compareAndSet(tail_cpy, tail_enq)) {
		        		return true;
	        		}
	        	}
        	}
    	}
   
        
    }

    public Integer deq() {
    	boolean done = false;
        Node tail_cpy;
        Node node_prev;
        Node node_deq;
        int retval;
        while(true) {
        	//start search
        	tail_cpy = this.tail.get();
        	node_prev = this.head.get();
        	node_deq = node_prev.next.get();
        	if (node_deq == null || tail_cpy == node_prev) {
            	//nothing to deq
            	return null;
            }
        	else {
        		retval = node_deq.value;
        		if(tail_cpy == node_deq) {
        			this.tail.compareAndSet(node_deq, this.head.get());
        			if(this.head.get().next.compareAndSet(node_deq, node_deq.next.get())) {
            			return retval;
        			}
        		}
        		else if(this.head.get().next.compareAndSet(node_deq, node_deq.next.get())) {
        			return retval;
    			}
        		}
    }
  }
    protected class Node {
        public Integer value;
        public AtomicReference<Node> next;

        public Node(Integer x) {
            value = x;
            next = new AtomicReference<Node>();
        }
    }
}
