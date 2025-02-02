package Homework2.q2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monkey {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition ropeIsEmpty = lock.newCondition();
    private static Condition ropeHasSpaceForMoreMonkeys = lock.newCondition();
    private static int numMonkeysOnRope = 0;
    private static int directionOnTheRope = 0;
    private static boolean kongIsOnTheRope = false;
    private static boolean kongWantsTheRope = false;

    public Monkey() { }

    // A monkey calls the method when it arrives at the river bank and wants to climb
    // the rope in the specified direction (0 or 1); Kong’s direction is -1.
    // The method blocks a monkey until it is allowed to climb the rope.
    public void ClimbRope(int direction) throws InterruptedException {

        lock.lock();

        try {
            // If I am Kong, wait for rope to be empty
            if (direction == -1) {
                this.kongWantsTheRope = true;

                // Wait for the other monkeys, and then climb
                while (this.numMonkeysOnRope > 0) {
                    ropeIsEmpty.await();
                }

                this.kongIsOnTheRope = true;
                this.kongWantsTheRope = false;
            }
            else // If I am not Kong
            {
                // I should wait
                //   - if Kong is on the rope
                //   - OR if Kong is waiting for the rope
                //   - OR if the other monkeys are not moving in my direction
                while (this.kongIsOnTheRope ||
                        this.kongWantsTheRope ||
                        (this.directionOnTheRope != direction && this.getNumMonkeysOnRope() > 0))
                {
                    this.ropeIsEmpty.await();
                }

                // Set the rope's direction to my direction
                this.directionOnTheRope = direction;

                while (this.numMonkeysOnRope >= 3 || this.kongIsOnTheRope)
                {
                    this.ropeHasSpaceForMoreMonkeys.await();
                }
            }

            // Climb the rope
            this.numMonkeysOnRope++;
            // System.out.println("Monkey going direction " + direction + " on the rope. Now there are " + this.numMonkeysOnRope);
        }
        finally
        {
            lock.unlock();

            // For testing
            // Thread.sleep(20);
        }
    }

    // After crossing the river, every monkey calls this method which
    // allows other monkeys to climb the rope.
    public void LeaveRope() {
        lock.lock();

        try {
            this.numMonkeysOnRope--;
            this.kongIsOnTheRope = false;

            if (this.numMonkeysOnRope == 0)
            {
                this.ropeIsEmpty.signalAll();
            }
            this.ropeHasSpaceForMoreMonkeys.signal();
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * Returns the number of monkeys on the rope currently for test purpose.
     *
     * @return the number of monkeys on the rope
     *
     * Positive Test Cases:
     * case 1: normal monkey (0 and 1)on the rope, this value should <= 3, >= 0
     * case 2: when Kong is on the rope, this value should be 1
     */
    public int getNumMonkeysOnRope()
    {
        return this.numMonkeysOnRope;
    }
}