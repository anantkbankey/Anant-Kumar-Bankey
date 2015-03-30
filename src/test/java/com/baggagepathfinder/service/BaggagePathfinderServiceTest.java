package com.baggagepathfinder.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.baggagepathfinder.exception.SystemException;
import com.baggagepathfinder.utils.FileUtils;

/**
 * The class BaggagePathfinderServiceTest tests BaggagePathfinderService with 
 * valid and invalid input set of data.
 * 
 * @author abankey
 *
 */
public class BaggagePathfinderServiceTest {
	private String[] testInputData = new String[3];
	private String[] testOutputData = new String[2];

	/**
	 * Load test data 
	 * 
	 * @throws IOException
	 */
	@Before
	public void setup() throws IOException{
		testInputData[0] = FileUtils.readFileData("/TestInputData_1.txt");
		testInputData[1] = FileUtils.readFileData("/TestInputData_2.txt");
		testInputData[2] = FileUtils.readFileData("/TestInputDataInvalid_3.txt");

		testOutputData[0] = FileUtils.readFileData("/TestOutputData_1.txt");
		testOutputData[1] = FileUtils.readFileData("/TestOutputData_2.txt");
	}

	/**
	 * It tests BaggagePathfinderService.findShortestPath method with 
	 * two set of input data.
	 */
	@Test
	public void testFindPath(){
		try{
			BaggagePathfinderService service = new BaggagePathfinderService();
			String output = service.findShortestPath(testInputData[0]);
			assertEquals(output, testOutputData[0]);

			output = service.findShortestPath(testInputData[1]);
			assertEquals(output, testOutputData[1]);
		}catch(SystemException e){
			fail(e.getMessage());
		}
	}

	/**
	 * It execute BaggagePathfinderService.findShortestPath method with 
	 * invalid input data. SystemException is expected.
	 * 
	 * @throws SystemException
	 */
	@Test(expected=SystemException.class)
	public void testFindPathException() throws SystemException{
		BaggagePathfinderService service = new BaggagePathfinderService();
		service.findShortestPath(testInputData[2]);
	}

	/**
	 * Clean up instance variables.
	 */
	@After
	public void tearDown(){
		testInputData = null;
		testOutputData = null;
	}
}
