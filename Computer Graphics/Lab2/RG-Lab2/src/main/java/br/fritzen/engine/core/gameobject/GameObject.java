package br.fritzen.engine.core.gameobject;

import java.util.ArrayList;

import org.joml.Vector3f;

import br.fritzen.engine.core.gameobject.components.Transform;
import br.fritzen.engine.core.gameobject.lights.BaseLight;
import br.fritzen.engine.rendering.RenderingSystem;


public class GameObject {

	private Scene gameScene;
	
	private GameObject parent;
	
	private ArrayList<GameObject> children;

	private ArrayList<GameComponent> components;
	
	private ArrayList<GameComponent> returnedComponents;
	
	private Transform transform;
	
	private String name;
	
	private boolean castShadow;
	
	
	public GameObject() {
		this("");
	}
	
	public GameObject(String name) {
	
		children = new ArrayList<GameObject>();
		components = new ArrayList<GameComponent>();
		returnedComponents = new ArrayList<GameComponent>();
		transform = new Transform();
		castShadow = true;
		this.name = name;
	}
	
	
	public void addChild(GameObject gameObject) {
		
		this.children.add(gameObject);
		
		gameObject.setParent(this);
		gameObject.getTransform().setParent(this.getTransform());
		gameObject.setScene(this.gameScene);
	}
	
	
	public void addGameComponent(GameComponent gameComponent) {
		
		this.components.add(gameComponent);
		gameComponent.setParent(this);
		
	}
	
	
	public void setParent(GameObject parent) {
		this.parent = parent;
	}
	
	
	public GameObject getParent() {
		return this.parent;
	}
	
	
	public Transform getTransform() {
		return this.transform;
	}


	public Scene getScene() {
		return gameScene;
	}


	public void setScene(Scene gameScene) {
		this.gameScene = gameScene;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
	public ArrayList<GameComponent> getComponents() {
		return this.components;
	}

	
	public ArrayList<GameObject> getChildren() {
		return this.children;
	}

	
	public void preProcess() {
		
		this.transform.preProcess();
		
		for (int i = 0; i < children.size(); i++)
			children.get(i).preProcess();

	}

	
	public void castShadow(boolean active) {
		this.castShadow = active;;
	}

	
	public boolean getCastShadow() {
		return this.castShadow;
	}

	
	public ArrayList<GameComponent> getComponentsByName(String compMeshRenderer) {
		
		returnedComponents.clear();
		
		for (int i = 0; i < components.size(); i++) {
			if (components.get(i).getComponentName().equals(compMeshRenderer)) {
				returnedComponents.add(components.get(i));
			}
		}
		
		return returnedComponents;
		
	}
	
	
	
}
