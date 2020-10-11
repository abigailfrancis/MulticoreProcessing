package Homework2.q2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class TestMonkeys {

    private final int[] monkeyDirectionList;

    /*
    Unit tests for various implementations of Cyclic Barrier
    This file covers the happy paths for various numParties and numThreads values
     */
    public TestMonkeys(final int[] monkeyDirectionList)
    {
        this.monkeyDirectionList = monkeyDirectionList;
    }

    @Parameterized.Parameters()
    public static Collection data() {

        ArrayList<Object> testParams = new ArrayList<Object>();

        // No direction change, no kong
        testParams.add(new int[] {0, 0, 0}); // 3 going left
        testParams.add(new int[] {1, 1, 1}); // 3 going right
        testParams.add(createLargeTest(100, 0, false));
        testParams.add(createLargeTest(0, 100, false));

        // One direction change, no Kong
        testParams.add(new int[] {0, 0, 0, 1, 1, 1}); // 3 going left & 3 going right
        testParams.add(new int[] {1, 1, 1, 0, 0, 0}); // 3 going right & 3 going left
        testParams.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}); // 10 going left & 1 going right
        testParams.add(new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}); // 10 going right & 1 going left
        testParams.add(new int[] {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}); // 1 going left, 10 going right
        testParams.add(new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}); // 1 going right, 10 going left

        // Multiple direction changes, no Kong
        testParams.add(new int[] {0, 1, 1, 0, 0, 1});
        testParams.add(new int[] {0, 1, 1, 0, 0, 1});
        testParams.add(createLargeTest(50, 50, false));
        testParams.add(createLargeTest(50, 50, false));


        // Only Kong
        testParams.add(new int[] {-1});

        // Kong is last
        testParams.add(new int[] {0, 0, 0, -1});
        testParams.add(new int[] {1, 1, 1, -1});
        testParams.add(new int[] {0, 0, 1, 0, 1, 0, -1});
        testParams.add(new int[] {1, 1, 0, 1, 0, 1, -1});

        // Kong is first
        testParams.add(new int[] {-1, 0, 0, 0});
        testParams.add(new int[] {-1, 1, 1, 1});
        testParams.add(new int[] {-1, 0, 0, 1, 0, 1, 0});
        testParams.add(new int[] {-1, 1, 1, 0, 1, 0, 1});

        // Kong is in the middle
        testParams.add(new int[] {0, -1, 0});
        testParams.add(new int[] {1, -1, 1});
        testParams.add(new int[] {0, 0, 1, -1, 1, 0, 0});
        testParams.add(new int[] {1, 1, 0, -1, 0, 1, 1});

        // Many Kongs
        testParams.add(new int[] {-1, -1, -1, -1, -1, -1});

        // Large tests
        testParams.add(createLargeTest(50, 50, true));

        return testParams;
    }

    @Test
    public void TestSynchronized() {
        MonkeyTester.runTest(monkeyDirectionList);
    }

    private static int[] createLargeTest(int numMonkeysLeft, int numMonkeysRight, boolean kongWantsToCross) {
        List<Integer> array = new ArrayList<Integer>();

        for (int i = 0; i < numMonkeysLeft; i++) {
            array.add(0);
        }

        for (int i = 0; i < numMonkeysRight; i++) {
            array.add(1);
        }

        if (kongWantsToCross) {
            array.add(-1);
        }

        // Shuffle the elements in the array
        Collections.shuffle(array);

        int[] retArray = new int[array.size()];
        for (int j = 0; j < array.size(); j++)
        {
            retArray[j] = array.get(j);
        }

        return retArray;
    }
}