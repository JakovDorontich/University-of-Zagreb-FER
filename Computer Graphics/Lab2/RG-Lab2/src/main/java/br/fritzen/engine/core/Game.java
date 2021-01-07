package br.fritzen.engine.core;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import br.fritzen.engine.core.gameloop.GameLoop;
import br.fritzen.engine.core.gameobject.GameObject;
import br.fritzen.engine.core.gameobject.Scene;
import br.fritzen.engine.core.input.Input;
import br.fritzen.engine.gui.GUI;
import br.fritzen.engine.rendering.RenderingSystem;

/**
 * Class used to represent and manage the Game
 * <br>
 * 
 * It uses:
 * <ul>
 * 		<li>{@link GameWindow} manipulated by GLFW
 * 		<li>{@link RenderingSystem} witch is responsible to render everything
 * 		<li>{@link Scene} graph Scene 
 * 		<li>{@link Input} the Input system used
 * 		<li>{@link GUI} the Graphical User Interface system (Menus, buttons, panels, ...)
 * </ul>
 * 
 * @author Vinicius Fritzen Machado
 *
 */
public abstract class Game extends GameLoop {

	private static GameWindow gameWindow;
	
	protected static Scene scene;
	
	private RenderingSystem renderer;
	
	private Input input;
	
	protected GUI gui;
	
	
	
	/**
	 * Default constructor for a new Game<br>
	 * Your class must extend it and @Override the abstract methods: <br>
	 * <ul> 
	 * <li> protected void init()
	 * <li> protected void update()
	 * <li> protected void input() 
	 * </ul>
	 * @param width initial size of window
	 * @param height initial size of window
	 * @param title window title
	 */
	public Game(int width, int height, String title) {
		
		Game.gameWindow = new GameWindow(width, height, title);
		
		this.input = Game.gameWindow.getInput();
		
		this.gui = new GUI(Game.gameWindow, this.input);
		this.renderer = new RenderingSystem(Game.gameWindow.getCapabilities(), this.gui);
		
		Game.scene = new Scene();
		
	}

	
	/**
	 * Start the game/engine
	 */
	public void start() {
		
		Game.gameWindow.show();
		super.run();
		
	}
	
	
	/**
	 * Get the current scene in the engine.
	 * @return the current {@link Scene}
	 */
	public static Scene getScene() {
		return scene;
	}
	
	
	/**
	 * Set the param Scene as the current {@link Scene}
	 * @param scene
	 */
	public void setScene(Scene scene) {
		Game.scene = scene;
	}
	

	/**
	 * Get the current gamewindow in the engine.
	 * @return the current {@link Scene}
	 */
	public static GameWindow getGameWindow() {
		return gameWindow;
	}
	
	
	/**
	 * Doesn't use this method or Override it unless you know what are you doing!
	 */
	@Override
	protected void input() {
		
		if (GLFW.glfwWindowShouldClose(Game.gameWindow.getWindowHandler())) {
			super.stop();
		}
		
		if (input.getClickedKey(GLFW.GLFW_KEY_F1) && input.getKey(GLFW.GLFW_KEY_LEFT_CONTROL)) {
			GS.GUI_FPS_DEBUG = !GS.GUI_FPS_DEBUG;
		}
		
		scene.processInputs();
		
	}

	
	/**
	 * Override this method for a specific update.
	 * When overriding you MUST call super.update();
	 */
	@Override
	protected void update() {
		
		this.gui.update();
		
		updateGameComponents(scene.getRootGameObject());
		scene.preProcess();
	}
	

	/**
	 * Doesn't use this method or Override it unless you know what are you doing!
	 */
	@Override
	protected void render() {
		
		if (gameWindow.hasSizeChanged())
			Game.scene.getCamera().updatePerspective(gameWindow.getAspect());
		
		renderer.render(Game.scene);
		
		GLFW.glfwSwapBuffers(Game.gameWindow.getWindowHandler());
		GLFW.glfwPollEvents();
		
	}
	
	
	/**
	 * Set a color as default ambient color
	 * @param color A {@link Vector4f} representing the Color (R G B A)
	 */
	protected void setAmbientColor(Vector4f color) {
		
		this.renderer.setClearColor(color);
		
	}
	
	
	/**
	 * Get the {@link Input used in the current system}
	 * @return
	 */
	public Input getInput() {
		return this.input;
	}
	
	
	/**
	 * Get the {@link GUI used in the current system}
	 * @return GUI root
	 */
	public GUI getGUI() {
		return this.gui;
	}
	
	
	/**
	 * 
	 * @return int[] array with ViewPort properties from windows, common [0, 0, width, height]
	 */
	public int[] getViewPort() {
		return Game.gameWindow.getViewPort();
	}
	
	
	/**
	 * Enable/Disable ShadowMaps 
	 */
	public void setShadowMap(boolean active) {
		
		this.renderer.setShadowMap(false);
	}
	
	
	/**
	 * Updates all scene / game components and their children
	 */
	protected void updateGameComponents(GameObject parent) {
		
		for (int i = 0; i < parent.getComponents().size(); i++) {
			parent.getComponents().get(i).update();
		}

		for (int i = 0; i < parent.getChildren().size(); i++) {
			updateGameComponents(parent.getChildren().get(i));
		}
		
	}
	
	
}
