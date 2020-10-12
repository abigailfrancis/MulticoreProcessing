package Homework2.q2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MonkeyTester implements Runnable {

    private int monkeyDirection;
    private Monkey monkey;

    public MonkeyTester(int monkeyDirection)
    {
        this.monkeyDirection = monkeyDirection;
        this.monkey = new Monkey();
    }

    public static void runTest(int[] monkeyDirectionList)
    {
        int numMonkeys = monkeyDirectionList.length;

        ExecutorService executorService = Executors.newFixedThreadPool(numMonkeys);
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < numMonkeys; i++) {
            futures.add(executorService.submit(new MonkeyTester(monkeyDirectionList[i])));
        }
        executorService.shutdown();

        // Wait for the results to become available
        for (int i = 0; i < numMonkeys; i++) {
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
        try {
            this.monkey.ClimbRope(this.monkeyDirection);
            this.monkey.LeaveRope();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
