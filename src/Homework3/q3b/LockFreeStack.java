package Homework3.q3b;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack implements MyStack {
    AtomicReference<Node> top;

    public LockFreeStack() {
        top = new AtomicReference<Node>();
    }

    public boolean push(Integer value) {
        try
        {
            Node node = new Node(value);
            while (true) {
                Node oldTop = top.get();
                node.next.set(oldTop);

                if (top.compareAndSet(oldTop, node)) {
                    return true;
                } else {
                    Thread.yield();
                }
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Integer pop() throws EmptyStack
    {
        while(true)
        {
            Node oldTop = top.get();
            if (oldTop == null)
            {
                throw new EmptyStack();
            }

            Integer val = oldTop.value;
            Node newTop = oldTop.next.get();
            if (top.compareAndSet(oldTop, newTop))
            {
                return val;
            }
            else
            {
                Thread.yield();
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
