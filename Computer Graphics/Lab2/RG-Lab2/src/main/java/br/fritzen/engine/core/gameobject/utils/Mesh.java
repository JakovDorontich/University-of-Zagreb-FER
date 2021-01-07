package br.fritzen.engine.core.gameobject.utils;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.gameobject.material.Material;
import br.fritzen.engine.core.gameobject.material.MaterialIndex;
import br.fritzen.engine.formats.obj.OBJLoader;
import br.fritzen.engine.model.IndexedModel;
import br.fritzen.engine.model.Vertex;
import br.fritzen.engine.rendering.shaders.GenericShader;
import br.fritzen.engine.utils.EngineBufferUtils;

public class Mesh {

	private Logger LOG = Logger.getLogger(Mesh.class.getName());
	
	private int vao;
	
	private int vbo;
	
	private int ibo;
	
	private int sizeIndices;
	
	private IndexedModel model;

	private Material blank;
	
	private float minx, maxx, miny, maxy, minz, maxz;
	
	private int drawMode = GL11.GL_TRIANGLES;
	
	private Vertex[] vertices;
	
	
	public Mesh(Vertex[] vertices, int[] indices) {
		
		this.sizeIndices = indices.length;
		this.vertices = vertices;
		
		this.vao = GL30.glGenVertexArrays();
		
		this.model = new IndexedModel();
		loadVertices(vertices, indices);
		
	}
	
		
	public Mesh(String filename) {
		
		OBJLoader loader = new OBJLoader();
		this.model = loader.load(filename);

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		for (int i = 0; i < model.getPositions().size(); i++) {
			vertices.add(new Vertex(model.getPositions().get(i), model.getTexCoords().get(i), model.getNormals().get(i), model.getTangents().get(i)));
		}

		if (GS.MESH_DEBUG)
			LOG.info("MODEL " +  filename + " LOADED. VERTICES: " + vertices.size());
		
		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);

		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);
			
		this.sizeIndices = indexData.length;
		this.vertices = vertexData;
		
		this.vao = GL30.glGenVertexArrays();
		
		loadVertices(vertexData, EngineBufferUtils.toIntArray(indexData));
		
	}
	
	
	public Mesh(IndexedModel model) {
		
		this.model = model;

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		for (int i = 0; i < model.getPositions().size(); i++) {
			vertices.add(new Vertex(model.getPositions().get(i), model.getTexCoords().get(i), model.getNormals().get(i)));
		}

		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);

		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);
			
		this.sizeIndices = indexData.length;
		this.vertices = vertexData;
		
		this.vao = GL30.glGenVertexArrays();
		
		loadVertices(vertexData, EngineBufferUtils.toIntArray(indexData));
		
	}
	
	
	/**
	 * Sends the model for the current shader
	 */
	public void draw() {
		
		GL30.glBindVertexArray(this.vao);
		GL11.glDrawElements(this.drawMode, sizeIndices, GL11.GL_UNSIGNED_INT, 0);
		
	}
	
	
	public void draw(GenericShader shader, Material defautlMaterial) {

		GL30.glBindVertexArray(this.vao);
		
		if (model.getMaterialIndices().size() == 0) {
		
			if (defautlMaterial == null) {
				blank.bind(shader);
			} else {
				defautlMaterial.bind(shader);
			}
			
			GL11.glDrawElements(this.drawMode, sizeIndices, GL11.GL_UNSIGNED_INT, 0);
		
		} else {
			
			if (defautlMaterial != null) {
			
				defautlMaterial.bind(shader);
				GL11.glDrawElements(this.drawMode, sizeIndices, GL11.GL_UNSIGNED_INT, 0);

			} else {
				
				for (int i = 0; i < model.getMaterialIndices().size(); i++) {
					
					MaterialIndex materialIndex = model.getMaterialIndices().get(i);
					model.getMaterialsByName(materialIndex.materialName).bind(shader);
					
					GL11.glDrawElements(this.drawMode, materialIndex.endIndex, GL11.GL_UNSIGNED_INT, Integer.BYTES * materialIndex.startIndex);

				}
			}
		}
		
	}
	
	
	private void loadVertices(Vertex[] vertices, int[] indices) {
		
		blank = new Material().createBlank();
		generateBoundingBox(vertices);
		
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
	
	
	private void generateBoundingBox(Vertex[] vertices) {
		
		minx = vertices[0].getPosition().x;
		maxx = vertices[0].getPosition().x;
		miny = vertices[0].getPosition().y;
		maxy = vertices[0].getPosition().y;
		minz = vertices[0].getPosition().z;
		maxz = vertices[0].getPosition().z;
		
		for (Vertex v : vertices) {
			
			if (v.getPosition().x < minx) {
				minx = v.getPosition().x;
			}
			
			if (v.getPosition().x > maxx) {
				maxx = v.getPosition().x;
			}
			
			if (v.getPosition().y < miny) {
				miny = v.getPosition().y;
			}
			
			if (v.getPosition().y > maxy) {
				maxy = v.getPosition().y;
			}
			
			if (v.getPosition().z < minz) {
				minz = v.getPosition().z;
			}
			
			if (v.getPosition().z > maxz) {
				maxz = v.getPosition().z;
			}
			
		}
		
	}
	
	
	public BoundingBox getBoundingBox() {
		return new BoundingBox(minx, maxx, miny, maxy, minz, maxz);
	}


	public void setDrawMode(int GL11_MODE) {
		
		this.drawMode = GL11_MODE;
		
	}
	
	
	public IndexedModel getModel() {
		return model;
	}

}
