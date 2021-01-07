package br.fritzen.engine.utils;

import org.joml.Vector3f;

/**
 * Useful and simplified methods
 * 
 * @author Vinicius Fritzen Machado
 *
 */
public class MyMath {

	public static float toRadians(float value) {
		return (float) Math.toRadians(value);
	}


	public static float toRadians(double value) {
		return (float) Math.toRadians(value);
	}
	
	
	public static Vector3f intersectionPointLinePlane(Vector3f start, Vector3f end, Vector3f A, Vector3f B, Vector3f C) {
		float x = 0, x1 = A.x, x2 = B.x, x3 = C.x;
        float y = 0, y1 = A.y, y2 = B.y, y3 = C.y;
        float z = 0, z1 = A.z, z2 = B.z, z3 = C.z;
        float[] xC = new float[]{x - x1, x2 - x1, x3 - x1};
        float[] yC = new float[]{y - y1, y2 - y1, y3 - y1};
        float[] zC = new float[]{z - z1, z2 - z1, z3 - z1};
        float addI = (yC[1] * zC[2]) - (yC[2] * zC[1]);
        float addJ = ((xC[1] * zC[2]) - (xC[2] * zC[1]));
        float addK = (xC[1] * yC[2]) - (xC[2] * yC[1]);

        float numOfTs = (addI * (end.x - start.x)) + (addJ * (end.y - start.y)) + (addK * (end.z - start.z));
        float num = (addI * (x1)) + (addJ * (y1)) + (addK * (z1)) - (addI * start.x) - (addJ * start.y) - (addK * start.z);
        float t = num / numOfTs;
        x = start.x + ((end.x - start.x) * t);
        y = start.y + ((end.y - start.y) * t);
        z = start.z + ((end.z - start.z) * t);
        
        return new Vector3f(x, y, z);
	}

	
}
