package q6;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class SynchronizedAdditionalTests
{
    private final int numThreads;
    private final int startC;

    /*
    Unit tests for PIncrement, using the Java keyword Synchornized
    This file covers the happy paths for various "c" and "numThreads" values
     */
    public SynchronizedAdditionalTests(final int input, final int startC)
    {
        this.numThreads = input;
        this.startC = startC;
    }

    @Parameterized.Parameters()
    public static Collection data()
    {
        var numThreadsVals = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
        var startCVals = new int[] {0, 100, 1234, -500};

        var testParams = new ArrayList<>();

        for (int i = 0; i < startCVals.length; i++) {
            for (int j = 0; j < numThreadsVals.length; j++) {
                testParams.add(new Object[] {numThreadsVals[j], startCVals[i]});
            }
        }

        return testParams;
    }

    @Test
    public void TestSynchronized() {
        int res = q6.Synchronized.PIncrement.parallelIncrement(this.startC, this.numThreads);

        assertTrue("Numthreads: " + this.numThreads + ", Start C: " + this.startC + " : Result is " + res + "", res == 1200000 + startC);
    }
}