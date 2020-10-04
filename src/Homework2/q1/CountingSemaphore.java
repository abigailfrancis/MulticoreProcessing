package Homework2.q1;

/* Implementation of a Counting Semaphore, as provided in class */
public class CountingSemaphore
{
    private int counter;

    /* Instantiates a new CountingSemaphore */
    public CountingSemaphore(int initVal)
    {
        counter = initVal;
    }

    public synchronized void P() throws InterruptedException {
        while (counter == 0)
        {
            wait();
        }
        counter--;
    }

    public synchronized void V()
    {
        counter++;
        notify();
    }
}