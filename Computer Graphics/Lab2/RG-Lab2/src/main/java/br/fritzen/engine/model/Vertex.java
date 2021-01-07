package br.fritzen.engine.model;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {

	public static final int SIZE = 11;
	
	private Vector3f position;
	
	private Vector2f texture;

	private Vector3f normal;
	
	private Vector3f tangent;
	
	
	public Vertex(Vector3f position) {
		this(position, new Vector2f(0.0f), new Vector3f(0.0f));
	}
	
	
	public Vertex(Vector3f position, Vector2f texture) {
		this(position, texture, new Vector3f(0.0f));		
	}
	
	
	public Vertex(Vector3f position, Vector2f texture, Vector3f normal) {
		this(position, texture, normal, new Vector3f(0f, 0f, 0f));	
		
	}
	
	
	public Vertex(Vector3f position, Vector2f texture, Vector3f normal, Vector3f tangent) {
		this.position = position;
		this.texture = texture;
		this.normal = normal;
		this.tangent = tangent;
	}
	
	
	public Vector3f getPosition() {
		return position;
	}

	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	
	public Vector2f getTexture() {
		return texture;
	}

	
	public void setTexture(Vector2f texture) {
		this.texture = texture;
	}


	public Vector3f getNormal() {
		return normal;
	}


	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
	
	
	public Vector3f getTangent() {
		return tangent;
	}


	public void setTangent(Vector3f tangent) {
		this.tangent = tangent;
	}


	public String toString() {
		return "Vertex:\n\tPosition: " + position.toString() + "\n\tTexture: " + texture.toString() + "\n\tNormal: " + normal.toString() + "\n\tTangent: " + tangent.toString() + "\n";
	}
}
