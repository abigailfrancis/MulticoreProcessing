package q6;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.Test;

import java.io.*;
import java.util.*;

//import org.junit.jupiter.api.Test;

public class TestPIncrement {

	@Test
	public void TestAtomicInteger() {
    	int res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 8);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 7);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 6);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 5);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 4);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 3);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 2);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 1);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
	}
	
	@Test
	public void TestSynchronized() {
		int res = q6.Synchronized.PIncrement.parallelIncrement(0, 8);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.Synchronized.PIncrement.parallelIncrement(0, 7);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.Synchronized.PIncrement.parallelIncrement(0, 6);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.Synchronized.PIncrement.parallelIncrement(0, 5);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.Synchronized.PIncrement.parallelIncrement(0, 4);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.Synchronized.PIncrement.parallelIncrement(0, 3);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.Synchronized.PIncrement.parallelIncrement(0, 2);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
    	
    	res = q6.Synchronized.PIncrement.parallelIncrement(0, 1);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
	}

}