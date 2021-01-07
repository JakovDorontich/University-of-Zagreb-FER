package br.fritzen.engine.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EngineFileUtils {

	public static String loadFile(final String filename) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		
		String line;
		
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		
		reader.close();
		
		return sb.toString();
		
	}
	
}
