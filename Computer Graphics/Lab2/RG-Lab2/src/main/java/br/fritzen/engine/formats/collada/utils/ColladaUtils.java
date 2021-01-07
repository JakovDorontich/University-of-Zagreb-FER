package br.fritzen.engine.formats.collada.utils;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class ColladaUtils {

	
	public static ArrayList<Vector3f> parseFloatToVec3(ArrayList<Float> data) {
		
		ArrayList<Vector3f> result = new ArrayList<Vector3f>();
		
		for (int i = 0; i < data.size() - 2; i+= 3) {
			
			result.add(new Vector3f(data.get(i), data.get(i+1), data.get(i+2)));
			
		}
		
		return result;
	}
	
	
	public static ArrayList<Vector2f> parseFloatToVec2(ArrayList<Float> data) {
		
		ArrayList<Vector2f> result = new ArrayList<Vector2f>();
		
		for (int i = 0; i < data.size() - 1; i+= 2) {
			
			result.add(new Vector2f(data.get(i), data.get(i+1)));
			
		}
		
		return result;
	}


	public static float[] parseStringToFloats(String valuesInString) {
		
		ArrayList<Float> result = ColladaUtils.parseStringToArrayFloats(valuesInString);
		
		float[] floatArray = new float[result.size()];
		int i = 0;

		for (Float f : result) {
		    //floatArray[i++] = (f != null ? f : Float.NaN);
			floatArray[i++] = f;
		}
		
		return floatArray;
	}
	
	
	public static ArrayList<Float> parseStringToArrayFloats(String valuesInString) {
		
		ArrayList<Float> result = new ArrayList<Float>();
		
		for (String s : valuesInString.split(" ")) {
			if (!s.trim().isEmpty()) {
				result.add(Float.parseFloat(s));
			}
		}
		
		return result;
	}
	
	
	public static ArrayList<Integer> parseStringToInts(String valuesInString) {
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for (String s : valuesInString.split(" ")) {
			if (!s.trim().isEmpty()) {
				result.add(Integer.parseInt(s));
			}
		}
		
		return result;
	}
}
