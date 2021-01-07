package br.fritzen.engine.terrain;

public class Terrain extends Node {

	private static TerrainShader terrainShader;
	
	private TerrainConfig terrainConfig;

		
	public void init(String file) {
		
		System.out.println("Terrain initilization");
		
		terrainShader = new TerrainShader(); 
		
		terrainConfig = new TerrainConfig();
		terrainConfig.loadFile(file);
	
		addChild(new TerrainQuadtree(terrainConfig));
		
	}
	
	
	public void updateQuadtree() {
		
		//Camera cam = Game.getScene().getCamera();
		//if (cam.getTransform().isMoved()) { TODO - WORK HERE TO IMPROVE PERFORMANCE
		
		if (true) {
			((TerrainQuadtree) getChildren().get(0)).updateQuadtree();
		}
		
	}
	
	
	public TerrainConfig getTerrainConfig() {
		return terrainConfig;
	}

	
	public void setTerrainConfig(TerrainConfig terrainConfig) {
		this.terrainConfig = terrainConfig;
	}
	
	
	public static TerrainShader getShader() {
		return terrainShader;
	}
	
}
