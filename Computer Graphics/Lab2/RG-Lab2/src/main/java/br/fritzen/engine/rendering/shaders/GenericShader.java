package br.fritzen.engine.rendering.shaders;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import br.fritzen.engine.core.gameobject.GameObject;
import br.fritzen.engine.core.gameobject.Scene;
import br.fritzen.engine.utils.EngineFileUtils;

public class GenericShader {

	private String filenameVertex;
	
	private String filenameFragment;
	
	private int shaderProgram;
	
	//private HashMap<String, Integer> attributesLocation = new HashMap<String, Integer>();
	private HashMap<String, Integer> uniformsLocation = new HashMap<String, Integer>();
	
	
	public GenericShader() {
		
		this.shaderProgram = GL20.glCreateProgram();
		
	}
	
	public GenericShader(final String filenameVertexShader, final String filenameFragmentShader, boolean ready) {
	
		this.filenameVertex = filenameVertexShader;
		this.filenameFragment = filenameFragmentShader;
		
		this.shaderProgram = GL20.glCreateProgram();
		
		this.setVertexShader(this.filenameVertex);
		this.setFragmentShader(this.filenameFragment);
		
		if (ready) {
			this.compileShader();
			
			this.bind();
			
			this.setAttributes();
			this.addAllUniforms();
		}
				
	}
	
	public GenericShader(final String filenameVertexShader, final String filenameFragmentShader) {
	
		this(filenameVertexShader, filenameFragmentShader, true);
		
//		this.filenameVertex = filenameVertexShader;
//		this.filenameFragment = filenameFragmentShader;
//		
//		this.shaderProgram = GL20.glCreateProgram();
//		
//		this.setVertexShader(this.filenameVertex);
//		this.setFragmentShader(this.filenameFragment);
//		
//		this.compileShader();
//		
//		this.bind();
//		
//		this.setAttributes();
//		this.addAllUniforms();
				
	}

	
	public void bind() {
		
		GL20.glUseProgram(this.shaderProgram);
		
	}
	
	
	public void unbind() {
		
		GL20.glUseProgram(0);
		
	}

	
	public void setAttributes() {
		
		GL20.glBindAttribLocation(this.shaderProgram, 0, "position");
		GL20.glBindAttribLocation(this.shaderProgram, 1, "texCoord");
		GL20.glBindAttribLocation(this.shaderProgram, 2, "normal");
		GL20.glBindAttribLocation(this.shaderProgram, 3, "tangent");
		
	}
	
	protected void addAllUniforms() {
		
		
		addUniform("cameraPosition");
		
		//MATERIAL
		addUniform("diffuse");
		addUniform("normalMap");
		
		addUniform("color");
		addUniform("specularPower");
		addUniform("specularIntensity");
		
		//MVP
		addUniform("model");
		addUniform("view");
		addUniform("projection");
		
	}
	
	
	public void updateSceneUniforms(Scene scene) {
		
		
		this.updateUniform("cameraPosition", scene.getCamera().getTransform().getPosition());
		
		GL20.glUniformMatrix4fv(uniformsLocation.get("view"), false, 
				scene.getCamera().getViewBuffer());
		
		GL20.glUniformMatrix4fv(uniformsLocation.get("projection"), false, 
				scene.getCamera().getProjectionBuffer());
	}
	
	
	public void updateObjectUniforms(GameObject gameObject) {
		
		GL20.glUniformMatrix4fv(uniformsLocation.get("model"), false, 
				gameObject.getTransform().getTransformationBuffer());
		
	}
	
	
	public HashMap<String, Integer> getUniformsLocation() {
		return uniformsLocation;
	}
	
	public void addUniform(String uniformName) {
		
		int location = GL20.glGetUniformLocation(this.shaderProgram, uniformName);
		
		if (location == 0xFFFFFFFF) {
			System.err.println("Error: Could not find uniform: " + uniformName + "\nCheck if it is being used!");
			new Exception().printStackTrace();
			System.exit(1);
		}
				
		uniformsLocation.put(uniformName, location);
	}
	
	
	@Override
	public void finalize() {
		//TODO UNIMPLEMENTED YET
	}

	protected void setVertexShader(String filenameVertexShader) {
		
		try {
			
			String shaderText = EngineFileUtils.loadFile(filenameVertexShader);
			this.addProgram(shaderText, GL20.GL_VERTEX_SHADER);
			
		} catch (IOException e) {
			System.err.println("Error loading file for VertexShader: " + filenameVertexShader);
			System.exit(1);
		}
		
	}


	protected void setFragmentShader(String filenameFragmentShader) {
		
		try {
			
			String shaderText = EngineFileUtils.loadFile(filenameFragmentShader);
			this.addProgram(shaderText, GL20.GL_FRAGMENT_SHADER);
			
		} catch (IOException e) {
			System.err.println("Error loading file for FragmentShader: " + filenameFragmentShader);
			System.exit(1);
		}
		
	}
	
	
	protected void setTesselationControlShader(String filenameTesselationControlShader) {
		
		try {
			
			String shaderText = EngineFileUtils.loadFile(filenameTesselationControlShader);
			this.addProgram(shaderText, GL40.GL_TESS_CONTROL_SHADER);
			
		} catch (IOException e) {
			System.err.println("Error loading file for TesselationControl: " + filenameTesselationControlShader);
			System.exit(1);
		}
		
	}
	
	
	protected void setTesselationEvaluationShader(String filenameTesselationEvaluationShader) {
		
		try {
			
			String shaderText = EngineFileUtils.loadFile(filenameTesselationEvaluationShader);
			this.addProgram(shaderText, GL40.GL_TESS_EVALUATION_SHADER);
			
		} catch (IOException e) {
			System.err.println("Error loading file for TesselationEvaluation: " + filenameTesselationEvaluationShader);
			System.exit(1);
		}
		
	}
	
	
	protected void setGeometryShader(String filenameGeometryShader) {
		
		try {
			
			String shaderText = EngineFileUtils.loadFile(filenameGeometryShader);
			this.addProgram(shaderText, GL32.GL_GEOMETRY_SHADER);
			
		} catch (IOException e) {
			System.err.println("Error loading file for GeometrySahder: " + filenameGeometryShader);
			System.exit(1);
		}
		
	}
	
	
	protected void setComputeShader(String filenameComputeShader) {
		
		try {
			
			String shaderText = EngineFileUtils.loadFile(filenameComputeShader);
			this.addProgram(shaderText, GL43.GL_COMPUTE_SHADER);
			
		} catch (IOException e) {
			System.err.println("Error loading file for ComputeSahder: " + filenameComputeShader);
			System.exit(1);
		}
		
	}
	

	private void addProgram(String text, int type) {
		
		int shader = GL20.glCreateShader(type);
		
		if (shader == 0) {
			System.err.println("Shader creation failed. Could not find valid memory location for it");
			System.exit(1);
		}
		
		GL20.glShaderSource(shader, text);
		GL20.glCompileShader(shader);
		
		if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) {
			System.err.println(GL20.glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		GL20.glAttachShader(this.shaderProgram, shader);
		
	}
	
	
	protected void compileShader() {

		GL20.glLinkProgram(this.shaderProgram);

		if (GL20.glGetProgrami(this.shaderProgram, GL20.GL_LINK_STATUS) == 0) {
			System.err.println("ERROR " + GL20.glGetShaderInfoLog(this.shaderProgram, 1024));
			System.exit(1);
		}

		GL20.glValidateProgram(this.shaderProgram);

		if (GL20.glGetProgrami(this.shaderProgram, GL20.GL_VALIDATE_STATUS) == 0) {
			System.err.println("ERROR " + GL20.glGetShaderInfoLog(this.shaderProgram, 1024));
			System.exit(1);
		}
	}


	public void updateUniform(String name, int value) {
		GL20.glUniform1i(uniformsLocation.get(name), value);
	}
	
	public void updateUniform(String name, float value) {
		GL20.glUniform1f(uniformsLocation.get(name), value);
	}
	
	public void updateUniform(String name, float x, float y) {
		GL20.glUniform2f(uniformsLocation.get(name), x, y);
	}
	
	public void updateUniform(String name, Vector2f vec) {
		GL20.glUniform2f(uniformsLocation.get(name), vec.x, vec.y);
	}
	
	public void updateUniform(String name, Vector3f vec) {
		GL20.glUniform3f(uniformsLocation.get(name), vec.x, vec.y, vec.z);
	}
	
	public void updateUniform(String name, float x, float y, float z) {
		GL20.glUniform3f(uniformsLocation.get(name), x, y, z);
	}
	
	//public void updateUniform(String name, Matrix4f mat) {
	//	GL20.glUniformMatrix4fv(uniformsLocation.get(name), false, mat.get(BufferUtils.createFloatBuffer(16)));
	//}

	public void updateUniform(String name, FloatBuffer buffer) {
		GL20.glUniformMatrix4fv(uniformsLocation.get(name), false, buffer);
	}

	public int getShaderProgram() {
		return shaderProgram;
	}
	
}
