package br.fritzen.engine.core.gameobject.utils;

import java.util.Random;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import br.fritzen.engine.core.gameobject.GameComponent;
import br.fritzen.engine.core.gameobject.material.Material;
import br.fritzen.engine.model.IndexedModel;
import br.fritzen.engine.model.Vertex;
import br.fritzen.engine.rendering.shaders.GenericShader;
import br.fritzen.engine.utils.EngineBufferUtils;

public class MeshPack extends GameComponent {

	private IndexedModel model;
	
	private Material material;
	
	private float radius;
	
	private int quantity;
	
	private boolean randomRotation;
	
	private int vao;
	
	private int vbo;
	
	private int ibo;
	
	private Vertex[] vertices;
	
	private  int[] indices;
	
	
	public MeshPack(IndexedModel model, Material material) {
	
		this.model = model;
		this.material = material;
		
		randomRotation = false;
		quantity = 1;
		radius = 0;
		
		
	}
	
	
	/**
	 * call it when you finish create your MeshPack, must be called just once. Do it in the init
	 */
	public void pack() {
		
		computeVertexAndIndices();
		
		this.vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(this.vao);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		
		
		this.vbo = GL15.glGenBuffers();
		this.ibo = GL15.glGenBuffers();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, EngineBufferUtils.createVertexBuffer(vertices), GL15.GL_STATIC_DRAW);
		
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 0);	// * 4 because 1 float has 4 bytes
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 12);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 20);
		GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 32);
		
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, EngineBufferUtils.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
		
		
		GL30.glBindVertexArray(0);
		

	}
	
	
	private void computeVertexAndIndices() {
		
		vertices = new Vertex[quantity * model.getIndices().size()];
		indices = new int[quantity * model.getIndices().size()];
		
		Random r = new Random();
		int counter = 0;
		
		for (int m = 0; m < quantity; m++) {

			float random = ( (r.nextFloat() - 0.5f) * 2 *  (float) Math.PI);
			float rx = (float) Math.sin(random) * r.nextFloat() * radius;
			float rz = (float) Math.cos(random) * r.nextFloat() *  radius;

			float rotateY = 0;
			
			if (randomRotation)
				rotateY = (float) (r.nextFloat() * Math.PI);

			for (int i = 0; i < model.getPositions().size(); i++) {
				
				vertices[counter] = new Vertex(
						new Vector3f(
								model.getPositions().get(i))
								.rotate(new Quaternionf(new AxisAngle4f(rotateY, new Vector3f(0, 1, 0)))).add(rx, 0, rz),
					
								model.getTexCoords().get(i), 
								model.getNormals().get(i));

				indices[counter++] = m * model.getIndices().size() + model.getIndices().get(i);
			}

		}
		
	}
	
	
	@Override
	public void render(GenericShader shader) {
		
		GL30.glBindVertexArray(this.vao);
		
		material.bind(shader);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
		
		
	}
	
	
	
	public boolean getRandomRotation() {
		return randomRotation;
	}


	public void setRandomRotation(boolean enable) {
		this.randomRotation = enable;
	}
	
	
	public float getRadius() {
		return radius;
	}


	public void setRadius(float radius) {
		this.radius = radius;
	}

	
	public int getQuantity() {
		return quantity;
	}

	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	@Override
	public String getComponentName() {
		return this.getClass().getSimpleName();
	}
	
}
