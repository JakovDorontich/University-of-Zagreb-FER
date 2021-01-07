package br.fritzen.engine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.joml.Vector2f;
import org.joml.Vector3f;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.gameobject.material.Material;
import br.fritzen.engine.core.gameobject.material.MaterialIndex;

public class IndexedModel {

	private final static Logger LOG = Logger.getLogger(IndexedModel.class.getName());
	
	private ArrayList<Vector3f> positions;

	private ArrayList<Vector2f> texCoords;

	private ArrayList<Vector3f> normals;

	private ArrayList<Vector3f> tangents;
	
	private ArrayList<Integer> indices;

	private ArrayList<Material> materials;

	private ArrayList<MaterialIndex> materialIndices;
	
	
	public IndexedModel() {
		this.positions = new ArrayList<Vector3f>();
		this.texCoords = new ArrayList<Vector2f>();
		this.normals = new ArrayList<Vector3f>();
		this.tangents = new ArrayList<Vector3f>();
		this.indices = new ArrayList<Integer>();
		this.materialIndices = new ArrayList<MaterialIndex>();
	}


	public IndexedModel(ArrayList<Vector3f> positions, ArrayList<Vector2f> texCoords, ArrayList<Vector3f> normals,
			ArrayList<Integer> indices) {
		
		this.positions = new ArrayList<Vector3f>();
		this.texCoords = new ArrayList<Vector2f>();
		this.normals = new ArrayList<Vector3f>();
		this.tangents = new ArrayList<Vector3f>();
		this.indices = new ArrayList<Integer>();
		this.materials = new ArrayList<Material>();
		this.materialIndices = new ArrayList<MaterialIndex>();
		
		toIndexedModel(positions, texCoords, normals, parseColladaIndices(indices), materials, materialIndices);
	}


	private ArrayList<Index> parseColladaIndices(ArrayList<Integer> colladaIndices) {
		
		ArrayList<Index> result = new ArrayList<Index>();
		
		for (int i = 0; i < colladaIndices.size() - 2; i += 3) {
			result.add(new Index(colladaIndices.get(i) + 1, colladaIndices.get(i+2) + 1, colladaIndices.get(i+1) +1));
		}
		
		return result;
	}


	public ArrayList<Vector3f> getPositions() {
		return positions;
	}


	public ArrayList<Vector2f> getTexCoords() {
		return texCoords;
	}


	public ArrayList<Vector3f> getNormals() {
		return normals;
	}


	public ArrayList<Integer> getIndices() {
		return indices;
	}

	
	public ArrayList<Vector3f> getTangents() {
		return tangents;
	}


	public void calcNormals() {

		System.out.println("HERE");
		if (GS.OBJLOADER_DEBUG)
			LOG.info("Processing normals");			
		
		for (int i = 0; i < indices.size(); i += 3) {
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vector3f v1 = new Vector3f();
			positions.get(i1).sub(positions.get(i0), v1);
			Vector3f v2 = new Vector3f();
			positions.get(i2).sub(positions.get(i0), v2);

			Vector3f normal = v1.cross(v2).normalize();

			normals.get(i0).add(normal);
			normals.get(i1).add(normal);
			normals.get(i2).add(normal);
		}

		for (int i = 0; i < normals.size(); i++)
			normals.get(i).set(normals.get(i).normalize());
	}

	
	public void calcTangents() {

		if (GS.OBJLOADER_DEBUG)
			LOG.info("Processing tangents");			
		
		for (int i = 0; i < indices.size(); i += 3) {
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vector3f v1 = new Vector3f();
			positions.get(i1).sub(positions.get(i0), v1);
			Vector3f v2 = new Vector3f();
			positions.get(i2).sub(positions.get(i0), v2);

			Vector2f t1 = new Vector2f();
			texCoords.get(i1).sub(texCoords.get(i0), t1);
			Vector2f t2 = new Vector2f();
			texCoords.get(i2).sub(texCoords.get(i0), t2);
			
			float r = 1f/(t1.x * t2.y - t1.y * t2.x);
			Vector3f tangent = new Vector3f();
			tangent.set(v1.mul(t2.y).sub(v2.mul(t1.y))).mul(r);
			
			tangents.get(i0).set(tangents.get(i0).add(tangent));
			tangents.get(i1).set(tangents.get(i1).add(tangent));
			tangents.get(i2).set(tangents.get(i2).add(tangent));
		}

		for (int i = 0; i < tangents.size(); i++) {
			tangents.get(i).normalize();
		}
	}
	

	public void setMaterials(ArrayList<Material> materials) {
		this.materials = materials;
	}

	
	public ArrayList<Material> getMaterials() {
		return this.materials;
	}


	public void setMaterialsIndices(ArrayList<MaterialIndex> materialIndicesHash) {
		this.materialIndices = materialIndicesHash;
	}


	public ArrayList<MaterialIndex> getMaterialIndices() {
		return materialIndices;
	}


	public Material getMaterialsByName(String materialName) {
		
		for (Material m : materials) {
			if (m.getName().equals(materialName))
				return m;
		}
		return null;
	}
	
	
	public IndexedModel toIndexedModel(ArrayList<Vector3f> positions, ArrayList<Vector2f> texCoords, ArrayList<Vector3f> normals,
			ArrayList<Index> indices, ArrayList<Material> materials, ArrayList<MaterialIndex> materialIndices) {

		IndexedModel result = new IndexedModel();
		IndexedModel normalModel = new IndexedModel();

		HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> normalIndexMap = new HashMap<Integer, Integer>();
		HashMap<Index, Integer> resultIndexMap = new HashMap<Index, Integer>();

		boolean hasTexCoords = false;
		if (texCoords != null) {
			if (texCoords.size() > 0) {
				hasTexCoords = true;
			}
		}
		
		boolean hasNormals = false;
		if (normals != null) {
			if (normals.size() > 0) {
				hasNormals = true;
			}
		}
		
		
		if (GS.OBJLOADER_DEBUG) {
			LOG.info("TOTAL OF MODEL INDICES: " + indices.size());
			LOG.info("TEXTURES: " + hasTexCoords);
			LOG.info("NORMALS: " + hasNormals);
		}	
		
		for (int i = 0; i < indices.size(); i++) {

			Index current = indices.get(i);
			Vector3f curPos = positions.get(current.vertexIndex - 1);
			Vector2f curTex;
			Vector3f curNormal;
			Vector3f curTangent = new Vector3f(0);

			if (hasTexCoords) {
				curTex = texCoords.get(current.textCoordIndex - 1);
			}
			else {
				curTex = new Vector2f(0, 0);
			}

			if (hasNormals) {
				curNormal = normals.get(current.normalIndex - 1);
			}
			else {
				curNormal = new Vector3f(0, 0, 0);
			}

			Integer modelVertexIndex = resultIndexMap.get(current);

			if (modelVertexIndex == null) {

				resultIndexMap.put(current, modelVertexIndex);
				modelVertexIndex = result.getPositions().size();

				result.getPositions().add(curPos);
				result.getTexCoords().add(curTex);
				result.getNormals().add(curNormal);
				result.getTangents().add(curTangent);
				
			} else {
				result.getIndices().add(modelVertexIndex);
			}

			Integer normalModelIndex = normalIndexMap.get(current.vertexIndex);

			if (normalModelIndex == null) {

				normalModelIndex = normalModel.getPositions().size();
				normalIndexMap.put(current.vertexIndex, normalModelIndex);

				normalModel.getPositions().add(curPos);
				normalModel.getTexCoords().add(curTex);
				normalModel.getNormals().add(curNormal);
				normalModel.getTangents().add(curTangent);
			}
			
			result.getIndices().add(modelVertexIndex);
			normalModel.getIndices().add(normalModelIndex);
			
			indexMap.put(modelVertexIndex, normalModelIndex);
		}
		
		if (!hasNormals) {
			normalModel.calcNormals();
		}
		
		result.calcTangents();
			
		for (int i = 0; i < result.getPositions().size(); i++) {
			result.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
		}

		this.positions = result.positions;
		this.normals = result.normals;
		this.tangents = result.tangents;
		this.texCoords = result.texCoords;
		this.indices = result.indices;
		
		this.materials = materials;
		this.materialIndices = materialIndices;
		if (materials.size() > 0) {
			this.materialIndices.get(materialIndices.size()-1).endIndex = indices.size();
			for (int i = 0; i < materialIndices.size() -1; i++) {
				materialIndices.get(i).endIndex = materialIndices.get(i+1).startIndex;
			}
		}
		
		return this;
	}
}
