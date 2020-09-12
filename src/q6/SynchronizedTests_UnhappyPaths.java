package q6;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SynchronizedTests_UnhappyPaths
{
    /*
    Unit tests for PIncrement, using the Java keyword synchronized
    This file covers the unhappy paths for various "c" and "numThreads" values
     */
    public SynchronizedTests_UnhappyPaths()
    {

    }

    @Test
    public void TestSynchronizedWithInvalidNumThreadsZero() {
        int res = q6.Synchronized.PIncrement.parallelIncrement(0, 0);
        assertEquals("Result is " + res + ", expected result is 1200000.", res, -1);
    }

    @Test
    public void TestSynchronizedWithInvalidNumThreadsNine() {
        int res = q6.Synchronized.PIncrement.parallelIncrement(0, 9);
        assertEquals("Result is " + res + ", expected result is 1200000.", res, -1);
    }
}