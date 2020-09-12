package q6;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdditionalTests_UnhappyPaths
{
    /*
    Unit tests for PIncrement
    This file covers the unhappy paths for various "c" and "numThreads" values
     */
    public AdditionalTests_UnhappyPaths()
    {

    }

    // Peterson's Tournament Algorithm
    // Todo

    // Synchronized
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

    // Atomic Integer
    @Test
    public void TestAtomicIntegerWithInvalidNumThreadsZero() {
        int res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 0);
        assertEquals("Result is " + res + ", expected result is 1200000.", res, -1);
    }

    @Test
    public void TestAtomicIntegerWithInvalidNumThreadsNine() {
        int res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 9);
        assertEquals("Result is " + res + ", expected result is 1200000.", res, -1);
    }

    // Reentrant Lock
    @Test
    public void TestReentrantLockWithInvalidNumThreadsZero() {
        int res = q6.ReentrantLock.PIncrement.parallelIncrement(0, 0);
        assertEquals("Result is " + res + ", expected result is 1200000.", res, -1);
    }

    @Test
    public void TestReentrantLockWithInvalidNumThreadsNine() {
        int res = q6.ReentrantLock.PIncrement.parallelIncrement(0, 9);
        assertEquals("Result is " + res + ", expected result is 1200000.", res, -1);
    }
}