package Homework2.q2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestMonkey {
    private final int[] direction;

    /*
    Unit tests for various implementations of Cyclic Barrier
    This file covers the happy paths for various numParties and numThreads values
     */
    public TestMonkey(final int[] direction) {
        this.direction = direction;
    }

    @Parameterized.Parameters()
    public static Collection data() {

        ArrayList<int[]> testParams = new ArrayList<int[]>();
      
        testParams.add(new int[]{1,1,1,1,-1,1,1,1});
        //testParams.add(new int[]{1,1,1,1,1,1,1,1,1,1,1});
        //testParams.add(new int[]{1,1,1,1,1,1,1,0});
        //testParams.add(new int[]{1,0,0,0});
        //testParams.add(new int[]{1,1,1,0,0,0});
        //testParams.add(new int[]{1,1,1,1,0,0,0,0,1,1,1,1,0,0,0,0});

        return testParams;
    }

    @Test
    public void TestSynchronized() {
        MonkeyTester.runTest(direction);
    }
}