package Homework2.q1;

public class CyclicBarrier {

    private final int numPartiesAllowedToEnter; // Number of threads required to trip the barrier
    private int numThreadsNeededToBreakBarrier; // Number of threads that the barrier is waiting on

    private CountingSemaphore mutex;
    private CountingSemaphore mutexForThreadEnteringBarrier;
    private CountingSemaphore mutexToWaitForThreadsToArrive;
    private CountingSemaphore mutexToWaitForThreadsToExecute;

    /* This class should only release threads only when
    the given number of threads are waiting upon it */
    public CyclicBarrier(int parties) {

        // Input validation
        if (parties <= 0)
        {
            throw new IllegalArgumentException();
        }

        // Initialize both variables to "parties"
        this.numPartiesAllowedToEnter = parties;
        this.numThreadsNeededToBreakBarrier = parties;

        // This mutex is the gatekeeper to the method
        this.mutex = new CountingSemaphore(this.numPartiesAllowedToEnter);

        // This mutex blocks all the threads until this.numThreadsNeededToBreakBarrier = this.numPartiesAllowedToEnter
        this.mutexToWaitForThreadsToArrive = new CountingSemaphore(0);

        // This mutex blocks all the threads until the last thread has had a chance to send parties-1 signals
        // to the mutexToWaitForThreadsToArrive
        this.mutexToWaitForThreadsToExecute = new CountingSemaphore(0);

        // This mutex is initialized to 1 because the first thread shouldn't be blocked from updating the variable,
        // but subsequent threads should be
        this.mutexForThreadEnteringBarrier = new CountingSemaphore(1);
    }


    int await() throws InterruptedException {

        // Top level Mutex allows up to 'parties' threads to enter the method
        this.mutex.P();

        // Local variable to store the current thread's arrival index
        int myArrivalIndex;


        /* ********* Critical section for setting/getting numThreadsNeededToBreakBarrier ********* */
        this.mutexForThreadEnteringBarrier.P();
        this.numThreadsNeededToBreakBarrier--;
        myArrivalIndex = this.numThreadsNeededToBreakBarrier;
        this.mutexForThreadEnteringBarrier.V();
        /* **************************************** */


        if (this.numThreadsNeededToBreakBarrier > 0)
        {
            this.mutexToWaitForThreadsToArrive.P();
        }
        else if (this.numThreadsNeededToBreakBarrier == 0)
        {
            this.reset();

            // The last thread to enter must signal to all other threads (numParties - 1)
            for (int i = 0; i < this.getNumParties() - 1; i++)
            {
                this.mutexToWaitForThreadsToArrive.V();
            }
        }


        /* ********* Critical section for setting/getting numThreadsNeededToBreakBarrier ********* */
        this.mutexForThreadEnteringBarrier.P();
        this.numThreadsNeededToBreakBarrier--;
        this.mutexForThreadEnteringBarrier.V();
        /* **************************************** */

        if (this.numThreadsNeededToBreakBarrier > 0)
        {
            this.mutexToWaitForThreadsToExecute.P();
        }
        else if (this.numThreadsNeededToBreakBarrier == 0)
        {
            this.reset();

            // The last thread to enter must signal to all other threads (numParties - 1)
            for (int i = 0; i < this.numPartiesAllowedToEnter - 1; i++)
            {
                this.mutexToWaitForThreadsToExecute.V();
            }
        }


        this.mutex.V();

        // Returns: the arrival index of the current thread, where index
        // (parties - 1) indicates the first to arrive and zero indicates
        // the last to arrive.
        return myArrivalIndex;
    }

    /* Getter to help the threads read the value of parties */
    public int getNumParties()
    {
        return this.numPartiesAllowedToEnter;
    }

    /* Reset the barrier */
    private void reset()
    {
        this.numThreadsNeededToBreakBarrier = this.numPartiesAllowedToEnter;
    }
}

