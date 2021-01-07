package br.fritzen.engine.terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class TerrainQuadtree  extends Node {

	private static int rootNodes = 8;

	
	public TerrainQuadtree(TerrainConfig config) {
	
		PatchVBO buffer = new PatchVBO();
		buffer.allocate(generateVertexData(), 16);
		
		
		for (int i = 0; i < rootNodes; i++) {

			for (int j = 0; j < rootNodes; j++) {
				addChild(new TerrainNode(buffer, config, new Vector2f(i / (float)rootNodes, j / (float) rootNodes), 0, new Vector2f(i, j)));
			}
		}
		
		getWorldTransform().setScale(new Vector3f(config.getScaleXZ(), config.getScaleY(), config.getScaleXZ()));
		getWorldTransform().setPosition(new Vector3f(-config.getScaleXZ()/2f, 0, -config.getScaleXZ()/2f));
		
		
	}
	
	
	public void updateQuadtree() {
		for (Node node : getChildren()) {
			((TerrainNode) node).updateQuadtree();
		}
	}
	
	
	public Vector2f[] generateVertexData() {
		
		Vector2f[] vertices = new Vector2f[16];
		
		int index = 0;
		
		vertices[index++] = new Vector2f(0,  0);
		vertices[index++] = new Vector2f(0.333f,  0);
		vertices[index++] = new Vector2f(0.666f,  0);
		vertices[index++] = new Vector2f(1f,  0);
		
		vertices[index++] = new Vector2f(0,  0.333f);
		vertices[index++] = new Vector2f(0.333f,  0.333f);
		vertices[index++] = new Vector2f(0.666f,  0.333f);
		vertices[index++] = new Vector2f(1f,  0.333f);
	
		vertices[index++] = new Vector2f(0,  0.666f);
		vertices[index++] = new Vector2f(0.333f,  0.666f);
		vertices[index++] = new Vector2f(0.666f,  0.666f);
		vertices[index++] = new Vector2f(1f,  0.666f);
		
		vertices[index++] = new Vector2f(0,  1f);
		vertices[index++] = new Vector2f(0.333f,  1f);
		vertices[index++] = new Vector2f(0.666f,  1f);
		vertices[index++] = new Vector2f(1f,  1f);
		
		return vertices;
	}
	
	
	public static int getRootNodes() {
		return rootNodes;
	}

	
}
