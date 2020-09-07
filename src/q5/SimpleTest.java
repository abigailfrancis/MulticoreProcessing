package q5;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTest {

    @Test
    public void TestBasic() {
        int[] A = {1};
        int frequency = Frequency.parallelFreq(1, A, 8);
        assertTrue("Result is " + frequency + ", expected frequency of 1 is 1.", frequency == 1);
    }

    @Test
    public void TestArray() {
        int[] A = {2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 33, 3, 3, 3, 3, 3, 3, 4, 5, 423, 2342, 423, 4, 23, 23, 423, 7, 23, 3, 23, 2, 5, 11, 232, 32, 32, 32, 65};
        int frequency = Frequency.parallelFreq(3, A, 8);
        assertTrue("Result is " + frequency + ", expected frequency of 3 is 19.", frequency == 19);
        frequency = Frequency.parallelFreq(23, A, 8);
        assertTrue("Result is " + frequency + ", expected frequency of 23 is 4.", frequency == 4);
        frequency = Frequency.parallelFreq(32, A, 8);
        assertTrue("Result is " + frequency + ", expected frequency of 32 is 3.", frequency == 3);
    }

    @Test
    public void MethodShouldReturnNegativeOneWhenNumThreadsIsInvalid() {
        int[] A = {2, 3, 3, 3, 3};
        int negativeNumThreads = -4;
        int frequency = Frequency.parallelFreq(3, A, negativeNumThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is -1.", frequency, -1);

        int zeroNumThreads = 0;
        frequency = Frequency.parallelFreq(3, A, zeroNumThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is -1.", frequency, -1);
    }

    @Test
    public void MethodShouldHandleWhenNumThreadsIsLargerThanArraySize() {
        int[] smallArray = {2, 3, 3};
        int largeNumThreads = 8;
        int frequency = Frequency.parallelFreq(3, smallArray, largeNumThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is 2.", 2, frequency);
    }

    @Test
    public void MethodShouldReturnZeroWhenArrayIsNullOrEmpty() {
        int[] emptyArray = {};
        int frequency = Frequency.parallelFreq(3, emptyArray, 8);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is 0.", 0, frequency);

        int[] nullArray = null;
        frequency = Frequency.parallelFreq(3, nullArray, 8);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is 0.", 0, frequency);
    }

    @Test
    public void MethodShouldComputeFrequencyOfNonPositiveNumbers() {
        int[] A = {-1, -1, -2, -3, 5, 0, -1, -2, 99, -1};
        int frequency = Frequency.parallelFreq(-1, A, 8);
        assertEquals("Result is " + frequency + ", expected frequency of -1 is 4.", 4, frequency);
        frequency = Frequency.parallelFreq(0, A, 8);
        assertEquals("Result is " + frequency + ", expected frequency of 0 is 1.", 1, frequency);
        frequency = Frequency.parallelFreq(-2, A, 8);
        assertEquals("Result is " + frequency + ", expected frequency of -2 is 2.", 2, frequency);
    }

    @Test
    public void MethodShouldComputeSameFrequencyForVariousValuesOfNumThreads() {
        int[] A = {3, 2, 4, 2, 6, 6, 3};
        int numThreads = 1;
        int frequency = Frequency.parallelFreq(2, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 2 is 2.", 2, frequency);
        frequency = Frequency.parallelFreq(3, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is 1.", 2, frequency);
        frequency = Frequency.parallelFreq(4, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 4 is 1.", 1, frequency);

        numThreads = 2;
        frequency = Frequency.parallelFreq(2, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 2 is 2.", 2, frequency);
        frequency = Frequency.parallelFreq(3, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is 1.", 2, frequency);
        frequency = Frequency.parallelFreq(4, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 4 is 1.", 1, frequency);

        numThreads = 5;
        frequency = Frequency.parallelFreq(2, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 2 is 2.", 2, frequency);
        frequency = Frequency.parallelFreq(3, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is 1.", 2, frequency);
        frequency = Frequency.parallelFreq(4, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 4 is 1.", 1, frequency);

        numThreads = 7;
        frequency = Frequency.parallelFreq(2, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 2 is 2.", 2, frequency);
        frequency = Frequency.parallelFreq(3, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is 1.", 2, frequency);
        frequency = Frequency.parallelFreq(4, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 4 is 1.", 1, frequency);
    }

    @Test
    public void MethodShouldReturnZeroIfTargetIsNotInArray() {
        int[] A = {3};
        int numThreads = 1;
        int frequency = Frequency.parallelFreq(2, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 2 is 0.", 0, frequency);
        frequency = Frequency.parallelFreq(3, A, numThreads);
        assertEquals("Result is " + frequency + ", expected frequency of 3 is 1.", 1, frequency);
    }
}