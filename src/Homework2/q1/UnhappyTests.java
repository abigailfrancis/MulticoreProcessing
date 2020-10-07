package Homework2.q1;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

@RunWith(Parameterized.class)
public class UnhappyTests {
    private final int numThreads;
    private final int numParties;

    /*
    Unit tests for various implementations of Cyclic Barrier
    This file covers the happy paths for various numParties and numThreads values
     */
    public UnhappyTests(final int numParties, final int numThreads) {
        this.numThreads = numThreads;
        this.numParties = numParties;
    }

    @Parameterized.Parameters()
    public static Collection data() {

        ArrayList<Object> testParams = new ArrayList<Object>();

        // For unhappy path scenarios, numParties will not divide evenly into numThreads
        testParams.add(new Object[]{2, 3});
        testParams.add(new Object[]{2, 5});
        testParams.add(new Object[]{2, 7});
        testParams.add(new Object[]{3, 4});
        testParams.add(new Object[]{3, 5});
        testParams.add(new Object[]{3, 7});
        testParams.add(new Object[]{4, 5});
        testParams.add(new Object[]{4, 6});
        testParams.add(new Object[]{4, 7});
        testParams.add(new Object[]{5, 6});
        testParams.add(new Object[]{5, 7});
        testParams.add(new Object[]{5, 8});
        testParams.add(new Object[]{6, 7});
        testParams.add(new Object[]{6, 8});
        testParams.add(new Object[]{7, 8});

        return testParams;
    }

    // All of these scenarios should time out
    @Test(timeout = 1000)
    public void TestSynchronized() {
        CyclicBarrierTester.runTest(numParties, numThreads);
    }
}