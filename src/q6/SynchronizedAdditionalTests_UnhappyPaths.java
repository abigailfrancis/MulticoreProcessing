package q6;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SynchronizedAdditionalTests_UnhappyPaths
{
    /*
    Unit tests for PIncrement, using the Java keyword Synchornized
    This file covers the happy paths for various "c" and "numThreads" values
     */
    public SynchronizedAdditionalTests_UnhappyPaths()
    {

    }

    @Test
    public void TestSynchronizedWithInvalidNumThreadsZero() {
        int res = q6.Synchronized.PIncrement.parallelIncrement(0, 0);
        assertTrue("Result is " + res + ", expected result is 1200000.", res == -1);
    }

    @Test
    public void TestSynchronizedWithInvalidNumThreadsNine() {
        int res = q6.Synchronized.PIncrement.parallelIncrement(0, 9);
        assertTrue("Result is " + res + ", expected result is 1200000.", res == -1);
    }
}