package br.fritzen.engine.core.gameobject.utils;

import java.util.Comparator;

import org.joml.Vector3f;

public class BoundingBoxComparator implements Comparator<BoundingBox> {

	private Vector3f position;
	
	
	public BoundingBoxComparator() {
	
	}
	
	
	@Override
	public int compare(BoundingBox b1, BoundingBox b2) {
		
		float dist1 = b1.getParent().getTransform().getTransformedPosition().distance(this.position);
		float dist2 = b2.getParent().getTransform().getTransformedPosition().distance(this.position);
		
		return Float.compare(dist1, dist2);
		
	}


	public void setRelativeTo(Vector3f relativeTo) {
		this.position = relativeTo;
	}

	
	
}
