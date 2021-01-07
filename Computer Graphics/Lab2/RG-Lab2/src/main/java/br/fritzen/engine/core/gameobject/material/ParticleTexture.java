package br.fritzen.engine.core.gameobject.material;

public class ParticleTexture {

	private Texture texture;
	private int numberOfRows;
	
	
	public ParticleTexture(Texture texture, int numberOfRows) {
		this.texture = texture;
		this.numberOfRows = numberOfRows;
		
	}


	public int getNumberOfRows() {
		return numberOfRows;
	}


	public Texture getTexture() {
		return texture;
	}
	
	
	
}
