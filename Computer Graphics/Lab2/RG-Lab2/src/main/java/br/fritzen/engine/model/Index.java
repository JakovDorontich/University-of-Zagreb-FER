package br.fritzen.engine.model;

public class Index {

	public int vertexIndex;

	public int textCoordIndex;

	public int normalIndex;

	public int tangentIndex;
	
	
	public Index() {
	
	}

	
	public Index(int vertexIndex, int textCoordIndex, int normalIndex) {
		super();
		this.vertexIndex = vertexIndex;
		this.textCoordIndex = textCoordIndex;
		this.normalIndex = normalIndex;
	}

	
	@Override
	public String toString() {
		return "Indexes:\n\t1. Vertex: " + vertexIndex + "\n\t2. Texture: " + textCoordIndex + "\n\t3. Normal: " + normalIndex + "\n\t4. Tangent: " + tangentIndex;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + normalIndex;
		result = prime * result + tangentIndex;
		result = prime * result + textCoordIndex;
		result = prime * result + vertexIndex;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Index other = (Index) obj;
		if (normalIndex != other.normalIndex)
			return false;
		if (tangentIndex != other.tangentIndex)
			return false;
		if (textCoordIndex != other.textCoordIndex)
			return false;
		if (vertexIndex != other.vertexIndex)
			return false;
		return true;
	}
	

}
