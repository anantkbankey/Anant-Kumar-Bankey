package com.baggagepathfinder.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.baggagepathfinder.service.BaggagePathfinderService;

/**
 * Contains utility methods for file operations.
 * @author abankey
 *
 */
public class FileUtils {

	/**
	 * Reads data from file
	 * 
	 * @param fileName Name of file
	 * 
	 * @return File content
	 * 
	 * @throws IOException
	 */
	public static String readFileData(String fileName) throws IOException{
		BufferedReader input = new BufferedReader(
				new InputStreamReader(FileUtils.class.getResourceAsStream(fileName)));
		StringBuilder data = new StringBuilder();
		try {
			String line = null;
			while (( line = input.readLine()) != null){
				data.append(line);
				data.append(BaggagePathfinderService.NEW_LINE);
			}
		}
		finally {
			input.close();
		}
		return data.toString();
	}

}
