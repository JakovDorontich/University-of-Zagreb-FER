package br.fritzen.engine.core.gameobject.material;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL11.glGenTextures;

public class TextureResource {

	private int id;

	private int refCount;


	public TextureResource() {
		this.id = glGenTextures();
		this.refCount = 1;
	}


	@Override
	protected void finalize() {
		glDeleteBuffers(id);
	}


	public int getId() {
		return id;
	}


	public void addReference() {
		this.refCount++;
	}


	public boolean removeReference() {
		this.refCount--;
		return this.refCount == 0;
	}
}
