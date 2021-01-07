package br.fritzen.engine.formats.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.joml.Vector2f;
import org.joml.Vector3f;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.gameobject.material.Material;
import br.fritzen.engine.core.gameobject.material.MaterialIndex;
import br.fritzen.engine.core.gameobject.material.Texture;
import br.fritzen.engine.model.Index;
import br.fritzen.engine.model.IndexedModel;


public class OBJLoader {

	private final static Logger LOG = Logger.getLogger(OBJLoader.class.getName());
	
	
	private ArrayList<Vector3f> positions;

	private ArrayList<Vector2f> texCoords;

	private ArrayList<Vector3f> normals;
	
	private ArrayList<br.fritzen.engine.model.Index> indices;
	
	private ArrayList<Material> materials;
	
	private ArrayList<MaterialIndex> materialIndices;
	
	private boolean hasTexCoords;
	
	private boolean hasNormals;
	
	private boolean processNormals;
		
	
	public OBJLoader() {
		
		hasTexCoords = false;
		hasNormals = false;
		processNormals = false;
		
		positions = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		indices = new ArrayList<Index>();
		materials = new ArrayList<Material>();
		
		materialIndices = new ArrayList<MaterialIndex>();
		
	}
	
	
	public IndexedModel load(final String filename, boolean processNormals) {
		this.processNormals = processNormals;
		return this.load(filename);
	}
	
	
	public IndexedModel load(final String filename) {
		
		if (GS.OBJLOADER_DEBUG)
			LOG.info("Loading obj file");
		
		BufferedReader fileReader = null;
		
		try {
			File file = new File(filename);
			fileReader = new BufferedReader(new FileReader(file));
		
			String line;
			
			while ((line = fileReader.readLine()) != null) {
				
				String[] tokens = line.split(" ");
				
				if (tokens[0].equals("mtllib")) {
					
					if (GS.OBJLOADER_DEBUG)
						LOG.info("Loading material file");
					
					String path = file.getCanonicalPath().substring(0, file.getCanonicalPath().lastIndexOf("\\"));
					materials = this.loadMaterials(path + "\\" + tokens[1]);
				
				} else if (tokens[0].equals("v")) {
					
					positions.add(new Vector3f(
							Float.parseFloat(tokens[1]),
							Float.parseFloat(tokens[2]),
							Float.parseFloat(tokens[3])
					));
					
				} else if (tokens[0].equals("vt")) {
					
					texCoords.add(new Vector2f(
							Float.parseFloat(tokens[1]),
							Float.parseFloat(tokens[2])
					));
					
				} else if (tokens[0].equals("vn")) {
					
					normals.add(new Vector3f(
							Float.parseFloat(tokens[1]),
							Float.parseFloat(tokens[2]),
							Float.parseFloat(tokens[3])
					));
					
				} else if (tokens[0].equals("f")) {
					
					for (int i = 0; i < tokens.length - 3; i++) {
						indices.add(parseOBJIndex(tokens[1]));
						indices.add(parseOBJIndex(tokens[2 + i]));
						indices.add(parseOBJIndex(tokens[3 + i]));
					}
					
				} else if (tokens[0].equals("usemtl")) {
					
					int currentIndex = indices.size();
					materialIndices.add(new MaterialIndex(tokens[1], currentIndex));
					
				}
				
			}
			
			
		} catch (FileNotFoundException e) {
		
			LOG.warning("Can't load file!!! Empty object has been created");
			e.printStackTrace();
		
		} catch (IOException e) {
			LOG.warning("Can't read file!!! Empty object has been created");
			e.printStackTrace();
		} finally {
			
			if (fileReader != null) {
			
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		return new IndexedModel().toIndexedModel(positions, texCoords, normals, indices, materials, materialIndices);
	}

	
	private ArrayList<Material> loadMaterials(String mtlFile) throws FileNotFoundException, IOException {
		
		ArrayList<Material> materials = new ArrayList<Material>();
		BufferedReader fileReader = new BufferedReader(new FileReader(mtlFile));
		Material currentMaterial = null;
		
		String line;
		while ((line = fileReader.readLine()) != null) {
			
			String tokens[] = line.split(" ");
			
			if (tokens[0].equals("newmtl")) {
				
				currentMaterial = new Material();
				currentMaterial.setName(tokens[1]);
				materials.add(currentMaterial);
				
			} else if (tokens[0].equals("Ka")) {
				
				currentMaterial.setAmbientColor(new Vector3f(
					Float.parseFloat(tokens[1]),
					Float.parseFloat(tokens[2]),
					Float.parseFloat(tokens[3])
					));
				
			} else if (tokens[0].equals("Kd")) {
			
				currentMaterial.setDiffuseColor(new Vector3f(
					Float.parseFloat(tokens[1]),
					Float.parseFloat(tokens[2]),
					Float.parseFloat(tokens[3])
					));
				
			} else if (tokens[0].equals("Ks")) {
			
				currentMaterial.setSpecularColor(new Vector3f(
					Float.parseFloat(tokens[1]),
					Float.parseFloat(tokens[2]),
					Float.parseFloat(tokens[3])
					));
					
			} else if (tokens[0].equals("Ke")) {
			
				currentMaterial.setEmissiveCoeficient(new Vector3f(
					Float.parseFloat(tokens[1]),
					Float.parseFloat(tokens[2]),
					Float.parseFloat(tokens[3])
					));
				
			} else if (tokens[0].equals("Ns")) {
				
				currentMaterial.setSpecularExponent(
					Float.parseFloat(tokens[1]));
				
			} else if (tokens[0].equals("Ni")) {
			
				currentMaterial.setRefractionIndex(
					Float.parseFloat(tokens[1]));
					
			} else if (tokens[0].equals("d")) {
			
				currentMaterial.setDissolveFactor(
					Float.parseFloat(tokens[1]));
				
			} else if (tokens[0].equals("illum")) {
				
				currentMaterial.setIlluminationModel(
						Integer.parseInt(tokens[1]));
				
			} else if (tokens[0].equals("map_Kd")) {
				
				String textureFile = "";
				if (tokens[1].startsWith("src")) {
					textureFile = tokens[1];
				} else {
					textureFile = mtlFile.substring(0, mtlFile.lastIndexOf("\\") + 1) + tokens[1];
				}
				
				Texture texture = new Texture(textureFile);
				currentMaterial.setTexture(texture);
			
			}
		}
		
		fileReader.close();
		
		if (materials.size() == 0) {
			materials.add(new Material().createDefaultMaterial());
		}
		return materials;
	}
	
	private Index parseOBJIndex(String token) {
		
		Index result = new Index();
		
		if (token.contains("//")) {
			
			String[] values = token.split("//");
			hasNormals = true;
			result.vertexIndex = Integer.parseInt(values[0]);
			result.normalIndex = Integer.parseInt(values[1]);
			result.textCoordIndex = 1;
		} else {
			
			String[] values = token.split("/");
	
			result.vertexIndex = Integer.parseInt(values[0]);
	
			if (values.length > 1) {
				hasTexCoords = true;
				result.textCoordIndex = Integer.parseInt(values[1]);
				if (values.length > 2) {
					hasNormals = true;
					result.normalIndex = Integer.parseInt(values[2]);
				}
			}
		}
		return result;
	}
	
}
