package br.fritzen.engine.terrain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import br.fritzen.engine.core.gameobject.material.Texture;
import br.fritzen.engine.rendering.NormalMapRenderer;
import br.fritzen.engine.utils.EngineUtils;

public class TerrainConfig {

	
	private float scaleY;
	private float scaleXZ;
	
	private Texture heightmap;
	private Texture normalmap;
	
	private int tessellationFactor;
	private float tessellationSlope;
	private float tessellationShift;
	
	private int[] lodRange = new  int[8];
	private int[] lodMorphingArea = new int[8];
	
	
	public void loadFile(String file) {
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				
				//System.out.println(line);
				
				String[] tokens = line.split(" ");
				tokens = EngineUtils.removeEmptyStrings(tokens);
				
				if (tokens.length == 0)
					continue;
				
				if (tokens[0].equals("scaleY")) {
					
					this.setScaleY(Float.valueOf(tokens[1]));
					
				} else if (tokens[0].equals("scaleXZ")) {
					this.setScaleXZ(Float.valueOf(tokens[1]));
				
				} else if (tokens[0].equals("heightmap")) {
					
					this.setHeightmap(new Texture(tokens[1]));
					this.getHeightmap().bilinearFilter();
					
					
					NormalMapRenderer normalMapRenderer = new NormalMapRenderer(this.getHeightmap().getWidth());
					normalMapRenderer.setStrength(10);
					normalMapRenderer.render(this.getHeightmap());
					
					this.setNormalMap(normalMapRenderer.getNormalMap());
					
					
				} else if (tokens[0].equals("tessellationFactor")) {
					this.setTessellationFactor(Integer.valueOf(tokens[1]));
				
				} else if (tokens[0].equals("tessellationSlope")) {
					this.setTessellationSlope(Float.valueOf(tokens[1]));
				
				} else if (tokens[0].equals("tessellationShift")) {
					this.setTessellationShift(Float.valueOf(tokens[1]));
				
				} else if (tokens[0].equals("lodRange")) {
					
					for (int i = 0; i < 8; i ++) {
						
						tokens = reader.readLine().split(" ");
						tokens = EngineUtils.removeEmptyStrings(tokens);
						
						if (tokens[0].equals("lod" + (i+1) + "_range")) {
							
							if (Integer.valueOf(tokens[1]) == 0) {
							
								lodRange[i] = 0;
								lodMorphingArea[i] = 0;
							
							} else {
								
								setLodRange(i, Integer.valueOf(tokens[1]));
								
							}
						}	
					}
				}
				
			}
			
		} catch (Exception e) {
			
			System.out.println("Error parsing terrain config file");
			
		} finally {
			
			if (reader != null) {
				
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	private int getMorphingArea(int lod) {
		return (int) ((scaleXZ/TerrainQuadtree.getRootNodes()) / (Math.pow(2, lod))); 
	}
	
	
	private void setLodRange(int index, int lodRange) {
		
		this.lodRange[index] = lodRange;
		this.lodMorphingArea[index] = lodRange - this.getMorphingArea(index + 1);
		
	}
	
	
	public float getScaleY() {
		return scaleY;
	}
	
	
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
	
	public float getScaleXZ() {
		return scaleXZ;
	}
	
	
	public void setScaleXZ(float scaleXZ) {
		this.scaleXZ = scaleXZ;
	}
	
	
	public int[] getLodRange() {
		return lodRange;
	}
	
	
	public void setLodRange(int[] lodRange) {
		this.lodRange = lodRange;
	}
	
	
	public int[] getLodMorphingArea() {
		return lodMorphingArea;
	}
	
	
	public void setLodMorphingArea(int[] lodMorphingArea) {
		this.lodMorphingArea = lodMorphingArea;
	}


	public int getTessellationFactor() {
		return tessellationFactor;
	}


	public void setTessellationFactor(int tessellationFactor) {
		this.tessellationFactor = tessellationFactor;
	}


	public float getTessellationSlope() {
		return tessellationSlope;
	}


	public void setTessellationSlope(float tessellationSlope) {
		this.tessellationSlope = tessellationSlope;
	}


	public float getTessellationShift() {
		return tessellationShift;
	}


	public void setTessellationShift(float tessellationShift) {
		this.tessellationShift = tessellationShift;
	}


	public Texture getHeightmap() {
		return heightmap;
	}


	public void setHeightmap(Texture heightmap) {
		this.heightmap = heightmap;
	}


	public Texture getNormalMap() {
		return normalmap;
	}


	public void setNormalMap(Texture normalmap) {
		this.normalmap = normalmap;
	}
	
	
}
