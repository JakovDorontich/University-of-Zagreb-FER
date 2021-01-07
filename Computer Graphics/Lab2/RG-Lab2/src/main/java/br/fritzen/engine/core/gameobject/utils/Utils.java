package br.fritzen.engine.core.gameobject.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.joml.Vector3f;

public class Utils {

	private static BoundingBoxComparator comparator = new BoundingBoxComparator();
	
	public static ArrayList<BoundingBox> sort(ArrayList<BoundingBox> boundboxList, Vector3f relativeTo) {
		
		comparator.setRelativeTo(relativeTo);
		Collections.sort(boundboxList, comparator);
		
		return boundboxList;
		
	}
	
}
