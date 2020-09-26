package q5;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.Test;

import java.io.*;
import java.util.*;

//import org.junit.jupiter.api.Test;

public class TestFcy {

    @Test
    public void TestThreadNums() {
    	int[] A = {1,0,0,0,0,0,1};
    	
    	/*TESTING ALL NUMBER OF THREADS ON PRIME NUMBER OF INPUTS, ENSURE THAT EDGES ARE FOUND*/
    	int find = 1;
    	int expected = 2;
    	
    	int threads = 7;
    	int frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == 2);
        
        threads = 6;
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == 2);
    	
        threads = 5;
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == 2);
        
        threads = 4;
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == 2);
        
        threads = 3;
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == 2);
    	
        threads = 2;
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == 2);
        
        threads = 1;
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == 2);
    	
    	
    }
    @Test
    public void TestNone() {
    	
    	/*TESTING EMPTY ARRAY*/
    	int[] A = {};
    	int find = 1;
    	int expected = 0;
    	int threads = 4;
    	int frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == expected);
        
        /*TESTING 0 THREADS*/
        int[] B = {1,1,1,1,1,1,1,1,1,1};
        threads = 0; 
        expected = -1;
        frequency = Frequency.parallelFreq(find, B, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == expected);
        
        /*TESTING NON ZERO ARRAY WITH NON EXIST FIND*/
        threads = 10;
        find = 0;
        expected = 0;
        frequency = Frequency.parallelFreq(find, B, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == expected);
        
    }
    
    @Test
    public void TestLargeSet() {
    	int[] A = new int[700];
    	int find = 1;
    	int expected = 0;
    	int threads = 4;
    	/*PARSE A CSV TO GET LARGE ARRAY FROM  A FILE*/
        String fileName= "LargeTest1.csv";
        File file= new File(fileName);
        // this gives you a 2-dimensional array of strings
        List<List<String>> lines = new ArrayList<>();
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);

            while(inputStream.hasNext()){
            	String line= inputStream.next();
                String[] values = line.split(",");
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(Arrays.asList(values));
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        /**********SET 1**********/
        
        for(int i = 0; i < lines.get(0).size(); i++ ) {
        	A[i] = Integer.valueOf(lines.get(0).get(i));
        }
        /*TESTING FOR 111 IN SET OF 700*/
       
        
        find = 111;
        expected = 1;
        threads = 10;
        
        int frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == expected);
    	
        
        /**********SET 2**********/
        
        for(int i = 0; i < lines.get(1).size(); i++ ) {
        	A[i] = Integer.valueOf(lines.get(1).get(i));
        }
        
        /*TESTING FOR 777 IN SET OF 700*/  
        find = 777;
        expected = 7;
        threads = 10;
        
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == expected);
    	
        /*TEST ODD NUMBER OF THREADS*/
        find = 777;
        expected = 7;
        threads = 687;
        
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == expected);
    	
        
        /**********SET 3**********/
        for(int i = 0; i < lines.get(3).size(); i++ ) {
        	A[i] = Integer.valueOf(lines.get(3).get(i));
        }
        /*TEST CORRECT NUMBER FOUND IN LARGE SET*/
        find = 2;
        expected = 700;
        threads = 10;
        
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == expected);
    	
        /*TEST NON EXIST VALUE*/
        find = 0;
        expected = 0;
        threads = 10;
        
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == expected);
    	
        /*TEST ODD NUMBER OF THREADS*/
        find = 2;
        expected = 700;
        threads = 687;
        
        frequency = Frequency.parallelFreq(find, A, threads);
        assertTrue("EXPECTED VALUE OF "+find+" = "+expected + "; THREADS = "+threads+ ";	RESULT = " + frequency , frequency == expected);
    	
        
        
    }

}