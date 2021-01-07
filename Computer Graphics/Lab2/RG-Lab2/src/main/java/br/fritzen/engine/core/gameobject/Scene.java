package br.fritzen.engine.core.gameobject;

import java.util.ArrayList;

import org.joml.Vector3f;

import br.fritzen.engine.core.gameobject.lights.DirectionalLight;
import br.fritzen.engine.core.gameobject.lights.PointLight;
import br.fritzen.engine.core.gameobject.lights.SpotLight;
import br.fritzen.engine.core.gameobject.lights.SunLight;
import br.fritzen.engine.core.gameobject.sceneobjects.Camera;
import br.fritzen.engine.core.gameobject.sceneobjects.ISky;
import br.fritzen.engine.rendering.FogModel;
import br.fritzen.engine.terrain.Terrain;

public class Scene {

	private GameObject root;
	
	private Camera camera;
	
	private String name;
	
	private Vector3f ambientLight;
	
	private SunLight sunLight;
	
	private ArrayList<GameComponent> allPointLights;

	private ArrayList<GameComponent> allSpotLights;

	private ArrayList<GameComponent> allDirectionalLights;
	
	private boolean hasSky;
	
	private ISky sky;

	private boolean hasTerrain;
	
	private Terrain terrain;
	
	private FogModel fog;
	
	
	public Scene() {
		
		this.ambientLight = new Vector3f(1.0f);
		this.sunLight = new SunLight(new Vector3f(1.0f), 0.0f, new Vector3f(0.0f));
		this.fog = new FogModel();
		this.fog.setHasFog(false);
		this.fog.setFogColor(new Vector3f(0.0f));
		
		allPointLights = new ArrayList<GameComponent>();
		allSpotLights =  new ArrayList<GameComponent>();
		allDirectionalLights =  new ArrayList<GameComponent>();
		
		hasSky = false;
		hasTerrain = false;
		
		this.root = new GameObject();
		this.root.setScene(this);
	}
	
	
	public void addGameObject(GameObject gameObject) {
		
		this.root.addChild(gameObject);
		
	}
	
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	
	public Camera getCamera() {
		return this.camera;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return this.name;
	}


	public GameObject getRootGameObject() {
		return this.root;
	}


	public Vector3f getAmbientLight() {
		return ambientLight;
	}


	public void setAmbientLight(Vector3f ambientLight) {
		this.ambientLight = ambientLight;
	}


	public SunLight getSunLight() {
		return sunLight;
	}


	public void setSunLight(SunLight sunLight) {
		this.sunLight = sunLight;
	}

	
	public void processInputs() {
		
		this.processAllInputs(this.getRootGameObject());
	
		//camera
		for (GameComponent gc : camera.getComponents()) {
			gc.input();
		}

	}
	
	
	private void processAllInputs(GameObject parent) {
		
		for (int i = 0; i < parent.getComponents().size(); i++) {
			parent.getComponents().get(i).input();
		}
		
		for (int i = 0; i < parent.getChildren().size(); i++) {
			processAllInputs(parent.getChildren().get(i));
		}
		
	}


	public void setSky(ISky sky) {
		
		this.sky = sky;
		this.hasSky = true;
		
	}

	
	public boolean hasSky() {
		return this.hasSky;
	}
	
	
	public void removeSky() {
		hasSky = false;
	}

	public ISky getSky() {
		return this.sky;
	}


	public void preProcess() {
		
		if (this.hasSky)
			this.sky.getTransform().preProcess();
		
		this.camera.preProcess();
		this.getRootGameObject().preProcess();
		
		this.processLights();
		
	}


	public void setTerrain(Terrain terrain) {
		hasTerrain = true;
		this.terrain = terrain;
	}
	
	
	public Terrain getTerrain() {
		return this.terrain;
	}


	public boolean hasTerrain() {
		return this.hasTerrain;
	}
	
	
	public void removeTerrain() {
		this.hasTerrain = false;
	}
	
	
	private void processLights() {
		
		allDirectionalLights.clear();
		allPointLights.clear();
		allSpotLights.clear();
		
		
		recursiveAddLights(this.getRootGameObject());
		
		sortByDistanceToCamera(allDirectionalLights);
		sortByDistanceToCamera(allPointLights);
		sortByDistanceToCamera(allSpotLights);
	
	}

	
	private void recursiveAddLights(GameObject parent) {
		
		for (int i = 0; i < parent.getComponents().size(); i++) {
			
			if (parent.getComponents().get(i).getComponentName().equals(DirectionalLight.class.getSimpleName())) {
				allDirectionalLights.add((DirectionalLight)parent.getComponents().get(i));
			
			} else if (parent.getComponents().get(i).getComponentName().equals(SpotLight.class.getSimpleName())) {
				allSpotLights.add((SpotLight)parent.getComponents().get(i));
			
			} else if (parent.getComponents().get(i).getComponentName().equals(PointLight.class.getSimpleName())) {
				allPointLights.add((PointLight)parent.getComponents().get(i));
			}
			
		}
		
		
		for (int i = 0; i < parent.getChildren().size(); i++) {
			recursiveAddLights(parent.getChildren().get(i));
		}
		
	}


	private void sortByDistanceToCamera(ArrayList<GameComponent> elements) {
	
		elements.sort((el1, el2) -> 
			Float.valueOf(el1.getParent().getTransform().getTransformedPosition().distance(this.getCamera().getCameraPos()))
			.compareTo(
			Float.valueOf(el2.getParent().getTransform().getTransformedPosition().distance(this.getCamera().getCameraPos()))		
			));
	
	}
	
	
	public ArrayList<GameComponent> getAllPointLights() {
		return this.allPointLights;
	}


	public ArrayList<GameComponent> getAllSpotLights() {
		return this.allSpotLights;
	}
	
	
	public FogModel getFogModel() {
		return this.fog;
	}
	
	
	public void setFogModel(FogModel fog) {
		this.fog = fog;
	}
}
