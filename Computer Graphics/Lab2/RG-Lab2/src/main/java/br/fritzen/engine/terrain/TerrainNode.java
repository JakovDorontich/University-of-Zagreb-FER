package br.fritzen.engine.terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

import br.fritzen.engine.core.Game;
import br.fritzen.engine.core.gameobject.sceneobjects.Camera;

public class TerrainNode extends Node {

	private boolean isLeaf;
	private TerrainConfig config;
	private int lod;
	private Vector2f location;
	private Vector3f worldPos;
	private Vector2f index;
	private float gap;
	private PatchVBO buffer;
	
	private int i;
	
	public TerrainNode(PatchVBO buffer, TerrainConfig config, Vector2f location, int lod, Vector2f index) {
	
		this.isLeaf = true;
		
		this.buffer = buffer;
		this.config = config;
		this.index = index;
		this.lod = lod;
		this.location = location;
		this.gap = 1f/(TerrainQuadtree.getRootNodes() * (float)(Math.pow(2, lod)));
		
		Vector3f locaScaling = new Vector3f(gap, 0, gap);
		Vector3f localTranslation = new Vector3f(location.x, 0, location.y);
		
		getLocalTransform().setScale(locaScaling);
		getLocalTransform().setPosition(localTranslation);
		
		getWorldTransform().setScale(new Vector3f(config.getScaleXZ(), config.getScaleY(), config.getScaleXZ()));
		getWorldTransform().setPosition(new Vector3f(-config.getScaleXZ()/2f, 0, -config.getScaleXZ()/2f));
	
		computeWorldPos();
		updateQuadtree();
		
	}

	
	@Override
	public void render() {
		
		if (isLeaf) {
			Terrain.getShader().updateUniforms(this);
			buffer.draw();
		}
		
		//for (Node child : getChildren()) {
		for (i = 0; i < getChildren().size(); i++) {
			getChildren().get(i).render();
		}
	}

	
	public void updateQuadtree() {
		
		Camera cam = Game.getScene().getCamera(); //TODO - GET CAMERA FROM CURRENT SCENE - verify
		
		if (cam.getTransform().getPosition().y >  config.getScaleY()) {
			
			worldPos.y = config.getScaleY();
		
		} else {

			worldPos.y = cam.getTransform().getPosition().y;
		}
		
		updateChildNode();
		
		for (Node node : getChildren()) {
			((TerrainNode) node).updateQuadtree();
		}
		
	}
	
	
	public void updateChildNode() {
		Vector3f camPos = new Vector3f(Game.getScene().getCamera().getTransform().getPosition());
		float distance = camPos.sub(worldPos).length();
		
		if (distance < config.getLodRange()[lod]) {
			addChildNodes(lod + 1);
		} else if (distance >= config.getLodRange()[lod]) {
			removeChildNodes();
		}
		
	}
	
	
	public void addChildNodes(int lod) {
		
		isLeaf = false;
		
		if (getChildren().size() == 0) {
			for (int i = 0; i < 2; i++) {
			
				for (int j = 0; j < 2; j++) {
					addChild(new TerrainNode(buffer, config, new Vector2f(location).add(i * gap/2f, j * gap/2f), lod, new Vector2f(i, j)));
				}
			}
			
			for (Node child : getChildren()) {
				child.setParent(this);
			}
		}
		
	}
	

	public void removeChildNodes() {
		
		
		isLeaf = true;
		
		if (getChildren().size() != 0) {
			getChildren().clear();
		}
		
	}
	
	
	public void computeWorldPos() {
	
		Vector2f loc = new Vector2f();
		loc.set(location).add(gap/2f, gap/2f).mul(config.getScaleXZ()).sub(config.getScaleXZ()/2f, config.getScaleXZ()/2f);
		this.worldPos = new Vector3f(loc.x, 0, loc.y);
	}
	
	
	public boolean isLeaf() {
		return isLeaf;
	}


	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}


	public TerrainConfig getConfig() {
		return config;
	}


	public void setConfig(TerrainConfig config) {
		this.config = config;
	}


	public int getLod() {
		return lod;
	}


	public void setLod(int lod) {
		this.lod = lod;
	}


	public Vector2f getLocation() {
		return location;
	}


	public void setLocation(Vector2f location) {
		this.location = location;
	}


	public Vector3f getWorldPos() {
		return worldPos;
	}


	public void setWorldPos(Vector3f worldPos) {
		this.worldPos = worldPos;
	}


	public Vector2f getIndex() {
		return index;
	}


	public void setIndex(Vector2f index) {
		this.index = index;
	}


	public float getGap() {
		return gap;
	}


	public void setGap(float gap) {
		this.gap = gap;
	}


	public PatchVBO getBuffer() {
		return buffer;
	}


	public void setBuffer(PatchVBO buffer) {
		this.buffer = buffer;
	}
	
}
