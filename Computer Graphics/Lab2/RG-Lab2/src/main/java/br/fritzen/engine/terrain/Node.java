package br.fritzen.engine.terrain;

import java.util.ArrayList;
import java.util.List;

import br.fritzen.engine.core.gameobject.components.Transform;

public class Node {

	
	private Node parent;
	
	private List<Node> children;
	
	private Transform worldTransform;
	
	private Transform localTransform;
	
	
	public Node() {
	
		this.setWorldTransform(new Transform());
		this.setLocalTransform(new Transform());
		this.setChildren(new ArrayList<Node>());
		
	}

	
	public void addChild(Node child) {
		//System.out.println("added");
		this.children.add(child);
	}
	
	
	public void update() {
		
		for (Node child : this.children) {
			child.update();
		}
	}

	
	public void input() {
		
		for (Node child : this.children) {
			child.input();
		}
	}
	

	public void render() {
		
		for (Node child : this.children) {
			child.render();
		}
	}
	
	
	public void shutdown() {
		
		for (Node child : this.children) {
			child.shutdown();
		}
	}
	
	
	public Node getParent() {
		return parent;
	}


	public void setParent(Node parent) {
		this.parent = parent;
	}


	public List<Node> getChildren() {
		return children;
	}


	public void setChildren(List<Node> children) {
		this.children = children;
	}


	public Transform getWorldTransform() {
		return worldTransform;
	}


	public void setWorldTransform(Transform worldTransform) {
		this.worldTransform = worldTransform;
	}


	public Transform getLocalTransform() {
		return localTransform;
	}


	public void setLocalTransform(Transform localTransform) {
		this.localTransform = localTransform;
	}
	
}
