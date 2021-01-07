package br.fritzen.engine.core.gameobject.material;

public class MaterialIndex {

	public MaterialIndex(String materialName, int startIndex) {
		this.materialName = materialName;
		this.startIndex = startIndex;
	}

	public String materialName;
	
	public int startIndex;
	
	public int endIndex;
}
