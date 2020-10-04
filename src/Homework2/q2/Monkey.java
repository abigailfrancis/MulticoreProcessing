package Homework2.q2;

public class Monkey {
    // declare the variables here

    // A monkey calls the method when it arrives at the river bank and wants to climb
    // the rope in the specified direction (0 or 1); Kong’s direction is -1.
    // The method blocks a monkey until it is allowed to climb the rope.
    public void ClimbRope(int direction) throws InterruptedException {

    }

    // After crossing the river, every monkey calls this method which
    // allows other monkeys to climb the rope.
    public void LeaveRope() {

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
    public int getNumMonkeysOnRope() {
        return 0;
    }
}