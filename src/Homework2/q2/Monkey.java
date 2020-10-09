package Homework2.q2;

import java.util.concurrent.locks.ReentrantLock;

public class Monkey {
    // declare the variables here
	ReentrantLock dir = new ReentrantLock();
	ReentrantLock Left = new ReentrantLock(); //0
	ReentrantLock Right = new ReentrantLock(); //1
	ReentrantLock kong = new ReentrantLock();
	private static int monkeysLeft = 0;
	private static int monkeysRight = 1;	
	private static int ropeDir = 0;
	private static boolean konglock = false;

    // A monkey calls the method when it arrives at the river bank and wants to climb
    // the rope in the specified direction (0 or 1); Kong’s direction is -1.
    // The method blocks a monkey until it is allowed to climb the rope.
    public void ClimbRope(int direction) throws InterruptedException {
    	boolean climb = false;
    	switch (direction) {
    		case 1 :		//monkey right
    			while(!climb) {
		    		//secure direction lock
		    		while(ropeDir != direction && konglock) {
		    			if(dir.tryLock()) {
		    				ropeDir = direction;
		    			}	    			
		    		}
		    		//wait for all of the right monkeys to get off
		    		while(monkeysLeft !=0);
		    		
		    		//wait to climb unless konglock
		    		while(!climb && !konglock && ropeDir == direction) {
		    			if (monkeysRight < 3) {
		    				if(Right.tryLock()) {
		    					monkeysRight ++;
		    					climb = true;
		    					Right.unlock();
		    				}//end if can grab the left lock
		    			}//end if there are less than 3 monkeys on the rope
		    		}//end while not climbing and konglock and direction	
		    	}//end while not climbing (right)
    			
    		case 0 :		//monkey left
		    	while(!climb) {
		    		//secure direction lock
		    		while(ropeDir != direction && konglock) {
		    			if(dir.tryLock()) {
		    				ropeDir = direction;
		    			}	    			
		    		}
		    		//wait for all of the right monkeys to get off
		    		while(monkeysRight !=0);
		    		
		    		//wait to climb unless konglock
		    		while(!climb && !konglock && ropeDir == direction) {
		    			if (monkeysLeft < 3) {
		    				if(Left.tryLock()) {
		    					monkeysLeft ++;
		    					climb = true;
		    					Left.unlock();
		    				}//end if can grab the left lock
		    			}//end if there are less than 3 monkeys on the rope
		    		}//end while not climbing and konglock and direction	
		    	}//end while not climbing (left)
	    	
		    	break;	    	
    	case  -1 : //kong
    				konglock = true;
    				while(monkeysRight != 0);
    				while(monkeysLeft != 0);
    				kong.lock();
        				
    			break;
    	}
    }

    // After crossing the river, every monkey calls this method which
    // allows other monkeys to climb the rope.
    public void LeaveRope() {
    	//if holds the direction lock, unlock
    	if(dir.isHeldByCurrentThread()) {
    		dir.unlock();
    	}
    	if(kong.isHeldByCurrentThread()) {
    		konglock = false;
    		kong.unlock();
    	}
    	else {
    		if (monkeysLeft > 0) {
    			Left.lock();
    			monkeysLeft--;
    			Left.unlock();
    		}
    		else if(monkeysRight > 0) {
    			Right.lock();
    			monkeysRight--;
    			Right.unlock();
    		}
    	}
    	//if self direction is left, decrement left
    	//if self direction is right, decrement right
    	//if self direction is kong, set kong to false
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
    	if (monkeysLeft > 0) {
			return monkeysLeft;
		}
		else if(monkeysRight > 0) {
			return monkeysRight;
		}
		else {
			return 1; //kong
		}
    }
}