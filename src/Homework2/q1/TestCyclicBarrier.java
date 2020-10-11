package Homework2.q1;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestCyclicBarrier {
    private final int numThreads;
    private final int numParties;

    /*
    Unit tests for various implementations of Cyclic Barrier
    This file covers the happy paths for various numParties and numThreads values
     */
    public TestCyclicBarrier(final int numParties, final int numThreads) {
        this.numThreads = numThreads;
        this.numParties = numParties;
    }

    @Parameterized.Parameters()
    public static Collection data() {

        ArrayList<Object> testParams = new ArrayList<Object>();

        // For happy path scenarios, numParties must divide evenly into numThreads
        testParams.add(new Object[]{8, 8});
        testParams.add(new Object[]{4, 8});
        testParams.add(new Object[]{2, 8});
        testParams.add(new Object[]{1, 8});
        testParams.add(new Object[]{7, 7});
        testParams.add(new Object[]{6, 6});
        testParams.add(new Object[]{5, 5});
        testParams.add(new Object[]{4, 4});
        testParams.add(new Object[]{2, 4});
        testParams.add(new Object[]{3, 3});
        testParams.add(new Object[]{2, 2});
        testParams.add(new Object[]{1, 2});
        testParams.add(new Object[]{1, 1});

        testParams.add(new Object[]{8, 24});
        testParams.add(new Object[]{6, 24});
        testParams.add(new Object[]{3, 24});
        testParams.add(new Object[]{2, 24});

        return testParams;
    }

    @Test
    public void TestSynchronized() {
        CyclicBarrierTester.runTest(numParties, numThreads);

        // Each arrival index (parties -1, parties -2... 0) should occur numThreads/numParties times
        int expectedCount = numThreads/ numParties;

        for (int i = 0; i < numParties; i++)
        {
            Assert.assertTrue(CyclicBarrierTester.expectedArrivalIndices.containsKey(i));

            Assert.assertTrue(CyclicBarrierTester.expectedArrivalIndices.get(i) == expectedCount);
        }
    }
}