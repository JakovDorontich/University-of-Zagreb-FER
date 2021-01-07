package br.fritzen.engine.core.gameobject.material;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import br.fritzen.engine.core.GS;

public class Texture {

	private static final Logger LOG = Logger.getLogger(Texture.class.getName());
	
	private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
	
	private TextureResource textureResource;
	
	private int width;
	private int height;
	
	
	public Texture() {
		textureResource = new TextureResource();
	}
	
	
	public Texture(String filename) {
		try {
			
			TextureResource resource = loadedTextures.get(filename);
			
			if (resource != null) {
				this.textureResource = resource;
				this.textureResource.addReference();
			
			} else {
				
				textureResource = this.loadTexture(filename);
				loadedTextures.put(filename, textureResource);
			}
			
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Can't load texture: " + filename);
			LOG.log(Level.SEVERE, e.toString());
		}
		
	}
	
	
	public void bind(int samplerSlot) {
		assert (samplerSlot >= 0 && samplerSlot < 31);
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + samplerSlot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureResource.getId());
	}


	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureResource.getId());
	}

	
	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	
	private TextureResource loadTexture(String filename) throws IOException {
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		ByteBuffer imageBuffer = STBImage.stbi_load(filename, width, height, channels, STBImage.STBI_rgb_alpha);
		
		int w = width.get();
		int h = height.get();
		int ch = channels.get();
		
		if (GS.TEXTURE_DEBUG)
			LOG.info("Texture loaded: " + filename + "\t Dimensions: " + w + ", " + h + ". Channels: " + ch);

		this.width = w;
		this.height = h;
		
		TextureResource textureResource = new TextureResource();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureResource.getId());			
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		
		return textureResource;
	}
	
	
	public void noFilter() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureResource.getId());			
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
	
	
	public void bilinearFilter() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureResource.getId());			
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	}

	
	public void repeatFiler() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
	}
	
	
	public int getId() {
		return this.textureResource.getId();
	}

	
	public int getWidth() {
		return width;
	}

	
	public int getHeight() {
		return height;
	}
	
	
	
}
