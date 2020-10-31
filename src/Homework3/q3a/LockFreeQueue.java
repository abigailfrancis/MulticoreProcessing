package Homework3.q3a;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue implements MyQueue {

    private AtomicReference<Node> head;
    private AtomicReference<Node> tail;

    public LockFreeQueue() {

        // Allocate a free node
        Node node = new Node(-1);

        // Make it the only node in the linked list
        node.next.set(null);

        // Both head and tail point to it
        head = new AtomicReference<>();
        tail = new AtomicReference<>();
        head.set(node);
        tail.set(node);
    }

    public boolean enq(Integer value) {
        // Allocate a new node
        // Copy enqueued value into node
        Node newNode = new Node(value);

        // Set next pointer of node to NULL
        newNode.next.set(null);

        Node tempTail;
        Node tempNext;

        try {
            // Keep trying until enqueue() is done
            while (true) {
                // Read tail and next
                tempTail = this.tail.get();
                tempNext = tempTail.next.get();

                // Are tail and next consistent?
                if (tempTail == this.tail.get()) {

                    // Was tail pointing to the last node?
                    if (tempNext == null) {

                        // Try to link node at the end of the linked list
                        if (tempTail.next.compareAndSet(tempNext, newNode)) {

                            // Enqueue is done. Exit loop.
                            break;
                        }
                    } else { // Tail was not pointing to the last node

                        // Try to swing tail to the next node
                        this.tail.compareAndSet(tempTail, tempNext);
                    }
                }
            }

            // Enqueue is done, Try to swing tail to the inserted node
            this.tail.compareAndSet(tempTail, newNode);
            return true;
        }
        catch(Exception e)
        {
            // The enqueue failed due to an exception
            return false;
        }
    }

    public Integer deq()
    {
        // Keep trying until dequeue is done
        while(true) {
            // Read head
            Node head = this.head.get();

            // Read tail
            Node tail = this.tail.get();

            // Read head -> next
            Node next = head.next.get();

            // Are head, tail, and next consistent?
            if (head == this.head.get())
            {
                // Is queue empty or tail failing behind?
                if (head == tail) {

                    // Is queue empty?
                    if (next == null) {

                        // Queue is empty, couldn't dequeue
                        return null;
                    }

                    // Tail is falling behind. Try to advance it.
                    this.tail.compareAndSet(tail, next);
                }
                else {
                    // Read value before CompareAndSet
                    // Otherwise, another dequeue might free the next ndoe
                    Integer dequeuedValue = next.value;

                    // Try to swing head to the next node
                    if (this.head.compareAndSet(head, next)) {
                        // TODO: Free the head?

                        // Dequeue is done. Exit the loop.
                        return dequeuedValue;
                    }
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
