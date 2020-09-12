package q6;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AtomicIntegerTests_UnhappyPaths
{
    /*
    Unit tests for PIncrement, using Atomic Integer
    This file covers the unhappy paths for various "c" and "numThreads" values
     */
    public AtomicIntegerTests_UnhappyPaths()
    {

    }

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
}