package br.fritzen.engine.rendering;

import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

import java.nio.FloatBuffer;
import java.util.logging.Logger;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLCapabilities;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.Game;
import br.fritzen.engine.core.gameobject.GameObject;
import br.fritzen.engine.core.gameobject.Scene;
import br.fritzen.engine.core.gameobject.lights.PointLight;
import br.fritzen.engine.core.gameobject.lights.SpotLight;
import br.fritzen.engine.gui.GUI;
import br.fritzen.engine.rendering.shaders.GenericShader;
import br.fritzen.engine.rendering.shadow.DepthShader;
import br.fritzen.engine.rendering.shadow.ShadowCascade;
import br.fritzen.engine.rendering.shadow.ShadowMap;
import br.fritzen.engine.terrain.Terrain;

public class RenderingSystem {

	private final static Logger LOG = Logger.getLogger(RenderingSystem.class.getName());

	private Vector4f clearColor;

	private GenericShader genericShader;

	private ShadowMap shadowMap[] = null;

	private ShadowCascade shadowCascade[];
	
	private DepthShader depthShader;

	private boolean hasShadowMap;

	private float cascadeEnd[] = {0.01f, 50f, 150f, 300f};
	
	private GUI gui;

	private static long renderingEndTime;
	
	private FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

	//AVOID CREATION OF STRINGS DYNAMICALLY
	private String cascadesEndClipSpace[] = {"cascadeEndClipSpace[0]","cascadeEndClipSpace[1]","cascadeEndClipSpace[2]"};
	private String lightsViewMatrix[] = {"lightViewMatrix[0]","lightViewMatrix[1]","lightViewMatrix[2]"};
	private String orthosProjectionMatrix[] = {"orthoProjectionMatrix[0]","orthoProjectionMatrix[1]","orthoProjectionMatrix[2]"};
	private String shadowsMap[] = {"shadowMap[0]","shadowMap[1]","shadowMap[2]"};
	
	
	public RenderingSystem(GLCapabilities glCapabilities, GUI gui) {

		LOG.info("Initializing Rendering System");
		LOG.info("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION) + " bytes");
		LOG.info("Max Geometry Uniform Blocks: " + GL31.GL_MAX_GEOMETRY_UNIFORM_BLOCKS + " bytes");
		LOG.info("Max Geometry Shader Invocations: " + GL40.GL_MAX_GEOMETRY_SHADER_INVOCATIONS + " bytes");
		LOG.info("Max Uniform Buffer Bindings: " + GL31.GL_MAX_UNIFORM_BUFFER_BINDINGS + " bytes");
		LOG.info("Max Uniform Block Size: " + GL31.GL_MAX_UNIFORM_BLOCK_SIZE + " bytes");
		LOG.info("Max SSBO Block Size: " + GL43.GL_MAX_SHADER_STORAGE_BLOCK_SIZE + " bytes");

		this.clearColor = new Vector4f(0.25f);
		GL.setCapabilities(glCapabilities);

		this.initGraphics();

		genericShader = new GenericShader("src/main/resources/shaders/genericVertexShader.glsl",
				"src/main/resources/shaders/genericFragmentShader.glsl");

		
		hasShadowMap = true;
		
		try {
			shadowMap = new ShadowMap[3];
			shadowCascade = new ShadowCascade[3];
			
			for (int i = 0; i < 3; i++) {
				shadowMap[i] = new ShadowMap();
				shadowCascade[i] = new ShadowCascade(cascadeEnd[i], cascadeEnd[i+1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		depthShader = DepthShader.getInstance();
		

		// add specific uniforms
		genericShader.addUniform("ambientLight");
		genericShader.addUniform("sunLight.base.color");
		genericShader.addUniform("sunLight.direction");
		genericShader.addUniform("sunLight.base.intensity");

		//point light uniforms
		genericShader.addUniform("currentPointLights");
		for (int i = 0; i < GS.MAX_POINT_LIGHTS; i++) {
			genericShader.addUniform("pointLight[" + i + "].base.color");
			genericShader.addUniform("pointLight[" + i + "].base.intensity");
			genericShader.addUniform("pointLight[" + i + "].atten.constant");
			genericShader.addUniform("pointLight[" + i + "].atten.linear");
			genericShader.addUniform("pointLight[" + i + "].atten.exponent");
			genericShader.addUniform("pointLight[" + i + "].position");
			genericShader.addUniform("pointLight[" + i + "].range");
		}
		
		//point light uniforms
		genericShader.addUniform("currentSpotLights");
		for (int i = 0; i < GS.MAX_POINT_LIGHTS; i++) {
			genericShader.addUniform("spotLight[" + i + "].point.base.color");
			genericShader.addUniform("spotLight[" + i + "].point.base.intensity");
			genericShader.addUniform("spotLight[" + i + "].point.atten.constant");
			genericShader.addUniform("spotLight[" + i + "].point.atten.linear");
			genericShader.addUniform("spotLight[" + i + "].point.atten.exponent");
			genericShader.addUniform("spotLight[" + i + "].point.position");
			genericShader.addUniform("spotLight[" + i + "].point.range");
			genericShader.addUniform("spotLight[" + i + "].cutOff");
			genericShader.addUniform("spotLight[" + i + "].coneDirection");
		}
		
		
		// SHADOW UNIFORMS
		genericShader.addUniform("hasShadow");

		genericShader.addUniform("lightViewMatrix[0]");
		genericShader.addUniform("lightViewMatrix[1]");
		genericShader.addUniform("lightViewMatrix[2]");
		
		genericShader.addUniform("shadowMap[0]");
		genericShader.addUniform("shadowMap[1]");
		genericShader.addUniform("shadowMap[2]");

		genericShader.addUniform("orthoProjectionMatrix[0]");
		genericShader.addUniform("orthoProjectionMatrix[1]");
		genericShader.addUniform("orthoProjectionMatrix[2]");

		genericShader.addUniform("cascadeEndClipSpace[0]");
		genericShader.addUniform("cascadeEndClipSpace[1]");
		genericShader.addUniform("cascadeEndClipSpace[2]");

		genericShader.addUniform("hasFog");
		genericShader.addUniform("fogDensity");
		genericShader.addUniform("fogGradient");
		genericShader.addUniform("fogColor");
		
		this.gui = gui;

	}

	
	public void render(Scene scene) {

		long renderingStartTime = System.currentTimeMillis();

		clearScreen();

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		if (scene.hasTerrain()) {

			Terrain.getShader().bind();
			scene.getTerrain().updateQuadtree();
			scene.getTerrain().render();
			Terrain.getShader().unbind();
		}

		if (scene.hasSky()) {
			scene.getSky().getShader().bind();
			scene.getSky().updateUniforms();
			scene.getSky().render();
			scene.getSky().getShader().unbind();
		}
	

		if (hasShadowMap) {
			renderShadowsToTexture(scene);
		}

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glViewport(0, 0, Game.getGameWindow().getWidth(), Game.getGameWindow().getHeight());

		this.genericShader.bind();
		this.updateSceneUniforms(scene);
		
		if (hasShadowMap) {
		
			for (int i = 0; i < GS.NUM_CASCADE_SHADOW_MAPS; i++) {
				
				this.genericShader.updateUniform(cascadesEndClipSpace[i], cascadeEnd[i + 1]);
				
				this.genericShader.updateUniform(lightsViewMatrix[i], shadowCascade[i].getLightViewMatrix().get(buffer));
				this.genericShader.updateUniform(orthosProjectionMatrix[i], shadowCascade[i].getOrthoProjMatrix().get(buffer));
		
					this.genericShader.updateUniform("hasShadow", 1);
					this.genericShader.updateUniform(shadowsMap[i], 2 + i); //texturas
					GL13.glActiveTexture(GL13.GL_TEXTURE2 + i);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadowMap[i].getDepthMapTexture().getId());
		
				
			}
			
		} else {
			this.genericShader.updateUniform("hasShadow", 0);
		}

		renderAll(this.genericShader, scene.getRootGameObject());
		
		renderAllOnce(scene.getRootGameObject());
		
		
		this.gui.render();

		renderingEndTime = (System.currentTimeMillis() - renderingStartTime);
		
		if (GS.RENDERING_SYSTEM_DEBUG)
			LOG.info("Scene Rendered in " + renderingEndTime + " miliSeconds");

	}

	
	private void renderAll(GenericShader shader, GameObject parent) {

		shader.updateObjectUniforms(parent);

		for (int i = 0; i < parent.getComponents().size(); i++) {
			parent.getComponents().get(i).render(shader);
		}

		for (int i = 0; i < parent.getChildren().size(); i++) {
			renderAll(shader, parent.getChildren().get(i));
		}

	}
	
	
	private void renderAllShadows(GenericShader shader, GameObject parent) {

		
		if (parent.getCastShadow()) {

			shader.updateObjectUniforms(parent);

			for (int i = 0; i < parent.getComponents().size(); i++) {
				parent.getComponents().get(i).render(shader);
			}
		} 
		
		for (int i = 0; i < parent.getChildren().size(); i++) {
			renderAllShadows(shader, parent.getChildren().get(i));
		}

	}
	

	private void renderAllOnce(GameObject parent) {

		for (int i = 0; i < parent.getComponents().size(); i++) {
			parent.getComponents().get(i).renderOnce();
		}

		for (int i = 0; i < parent.getChildren().size(); i++) {
			renderAllOnce(parent.getChildren().get(i));
		}

	}

	
	private void renderShadowsToTexture(Scene scene) {

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_FRONT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		for (int i = 0; i < 3; i++) {
			
			shadowCascade[i].update(scene.getCamera().getView(), scene.getSunLight());
			
			glBindFramebuffer(GL_FRAMEBUFFER, shadowMap[i].getDepthMapFBO());
	
			GL11.glViewport(0, 0, ShadowMap.SHADOW_MAP_WIDTH, ShadowMap.SHADOW_MAP_HEIGHT);
	
			this.clearScreen();
	
			this.depthShader.bind();
			
			this.depthShader.lightViewMatrix = shadowCascade[i].getLightViewMatrix();
			this.depthShader.updateUniforms(shadowCascade[i].getOrthoProjMatrix());
			renderAllShadows(depthShader, scene.getRootGameObject());
			
			depthShader.unbind();
			glBindFramebuffer(GL_FRAMEBUFFER, 0);
			
		}
		
		GL11.glCullFace(GL11.GL_BACK);
	}

	
	private void updateSceneUniforms(Scene scene) {

		this.genericShader.updateSceneUniforms(scene);

		this.genericShader.updateUniform("ambientLight", scene.getAmbientLight());

		this.genericShader.updateUniform("sunLight.base.color", scene.getSunLight().getColor());
		this.genericShader.updateUniform("sunLight.direction", scene.getSunLight().getDirection());
		this.genericShader.updateUniform("sunLight.base.intensity", scene.getSunLight().getIntensity());

		updateScenePointLights(scene);
		updateSceneSpotLights(scene);
		
		
		if (scene.getFogModel().isHasFog()) {
			this.genericShader.updateUniform("hasFog", 1);
			this.genericShader.updateUniform("fogDensity", scene.getFogModel().getFogDensity());
			this.genericShader.updateUniform("fogGradient", scene.getFogModel().getFogGradient());
			this.genericShader.updateUniform("fogColor", scene.getFogModel().getFogColor());
			
		} else {
			this.genericShader.updateUniform("hasFog", 0);
		}
		
	}

	
	private void updateScenePointLights(Scene scene) {
		
		genericShader.updateUniform("currentPointLights", scene.getAllPointLights().size());
		for (int i = 0; i < scene.getAllPointLights().size() && i < GS.MAX_POINT_LIGHTS; i++) {
		
			PointLight light = (PointLight) scene.getAllPointLights().get(i);
		
			genericShader.updateUniform("pointLight[" + i + "].base.color", light.getColor());
			genericShader.updateUniform("pointLight[" + i + "].base.intensity", light.getIntensity());
			genericShader.updateUniform("pointLight[" + i + "].atten.constant", light.getAttenuation().getConstant());
			genericShader.updateUniform("pointLight[" + i + "].atten.linear", light.getAttenuation().getLinear());
			genericShader.updateUniform("pointLight[" + i + "].atten.exponent", light.getAttenuation().getExponent());
			genericShader.updateUniform("pointLight[" + i + "].position", light.getParent().getTransform().getTransformedPosition());
			genericShader.updateUniform("pointLight[" + i + "].range", light.getRange());
			
		}
	}
	
	
	private void updateSceneSpotLights(Scene scene) {
		
		genericShader.updateUniform("currentSpotLights", scene.getAllSpotLights().size());
		for (int i = 0; i < scene.getAllSpotLights().size() && i < GS.MAX_SPOT_LIGHTS; i++) {
		
			SpotLight light = (SpotLight) scene.getAllSpotLights().get(i);
		
			genericShader.updateUniform("spotLight[" + i + "].point.base.color", light.getColor());
			genericShader.updateUniform("spotLight[" + i + "].point.base.intensity", light.getIntensity());
			genericShader.updateUniform("spotLight[" + i + "].point.atten.constant", light.getAttenuation().getConstant());
			genericShader.updateUniform("spotLight[" + i + "].point.atten.linear", light.getAttenuation().getLinear());
			genericShader.updateUniform("spotLight[" + i + "].point.atten.exponent", light.getAttenuation().getExponent());
			genericShader.updateUniform("spotLight[" + i + "].point.position", light.getParent().getTransform().getTransformedPosition());
			genericShader.updateUniform("spotLight[" + i + "].point.range", light.getRange());
			genericShader.updateUniform("spotLight[" + i + "].cutOff", light.getCutOff());
			genericShader.updateUniform("spotLight[" + i + "].coneDirection", light.getConeDirection());
		}
	}
	
	
	
	public void clearScreen() {

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ACCUM_BUFFER_BIT
				| GL11.GL_STENCIL_BUFFER_BIT);

	}

	
	public Vector4f getClearColor() {
		return clearColor;
	}

	
	public void setClearColor(Vector4f clearColor) {

		this.clearColor = clearColor;
		this.setClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);

	}

	
	private void setClearColor(float r, float g, float b, float a) {

		GL11.glClearColor(r, g, b, a);
	}

	
	private void initGraphics() {

		// GL11.glEnable(GL11.GL_CULL_FACE);
		// GL11.glCullFace(GL11.GL_BACK);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glEnable(GL11.GL_STENCIL_TEST);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	
	public static long getRenderingTime() {
		return renderingEndTime;
	}

	
	public void setShadowMap(boolean active) {
		this.hasShadowMap = active;
	}



}
