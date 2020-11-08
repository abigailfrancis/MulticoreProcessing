package Homework3.q3b;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack implements MyStack {
    // you are free to add members
	private AtomicReference<Node> head;
    public LockFreeStack() {
        // implement your constructor here
    	//create head and tail
    	head = new AtomicReference<>();
    	//create initial node in list which is both head and tail
    	Node initNode = new Node(-1);

    	head.set(initNode);
    }

    public boolean push(Integer value) {
    	//create new node to push
    	Node head_push = new Node(value);
    	Node head_cpy; //copy of the current head
    	
    	//try continuously to push node until done

    	while(true) {
    		//get head 
    		head_cpy = this.head.get();
    		//set new node to point to head
    		head_push.next.set(head_cpy);	
    		//try to set the head to the new node
        	if(this.head.compareAndSet(head_cpy, head_push)) {
        		return true;
        	}
		}
    }

    public Integer pop() throws EmptyStack {
        // implement your pop method here
    	int retval;
    	Node head_cpy;

    	while(true) {
    		head_cpy = this.head.get();
    		retval = head_cpy.value;
    		if(head_cpy.next.get() == null) {
    			throw new EmptyStack();
    		}
	    	else if(this.head.compareAndSet(head_cpy, head_cpy.next.get())) {
				return retval;
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

