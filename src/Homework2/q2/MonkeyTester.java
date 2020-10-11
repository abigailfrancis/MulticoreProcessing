package Homework2.q2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MonkeyTester implements Runnable {

    public static Monkey monkey;
    public int direction;
    public int monkeynumber;

    public MonkeyTester(int direction, int monkeynumber)
    {
        this.direction = direction;
        this.monkeynumber = monkeynumber;
    }

    public static void runTest(int[] direction)
    {
        monkey = new Monkey();

        ExecutorService executorService = Executors.newFixedThreadPool(direction.length);
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < direction.length; i++) {
            futures.add(executorService.submit(new MonkeyTester(direction[i], i)));
        }
        executorService.shutdown();

        // Wait for the results to become available
        for (int i = 0; i < direction.length; i++) {
            try {
                futures.get(i).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        // System.out.println(Thread.currentThread().getName() + " is waiting for all other threads to reach common barrier point");
    	System.out.println(this.monkeynumber+ " Direction : "+ this.direction + " before climb: Number of monkeys on the rope is "+monkey.getNumMonkeysOnRope());
        try {
			monkey.ClimbRope(this.direction);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        System.out.println(this.monkeynumber + " Direction : "+ this.direction + " climbing: Number of monkeys on the rope is "+monkey.getNumMonkeysOnRope());
        monkey.LeaveRope();
        System.out.println(this.monkeynumber + " Direction : "+ this.direction + " after climbing: Number of monkeys on the rope is "+monkey.getNumMonkeysOnRope());
           // System.out.println("Arrival index: " + myArrivalIndex);
    }
}