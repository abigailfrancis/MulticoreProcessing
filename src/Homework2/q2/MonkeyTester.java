package Homework2.q2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonkeyTester implements Runnable {

    private int monkeyDirection;
    private Monkey monkey;

    public MonkeyTester(int monkeyDirection, ReentrantLock lock, Condition isEmpty, Condition hasRoom)
    {
        this.monkeyDirection = monkeyDirection;
        this.monkey = new Monkey(lock, isEmpty, hasRoom);
    }

    public static void runTest(int[] monkeyDirectionList)
    {
        ReentrantLock lock = new ReentrantLock();
        Condition ropeIsEmpty = lock.newCondition();
        Condition ropeHasSpaceForMoreMonkeys = lock.newCondition();

        int numMonkeys = monkeyDirectionList.length;

        ExecutorService executorService = Executors.newFixedThreadPool(numMonkeys);
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < numMonkeys; i++) {
            futures.add(executorService.submit(new MonkeyTester(monkeyDirectionList[i], lock, ropeIsEmpty, ropeHasSpaceForMoreMonkeys)));
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
