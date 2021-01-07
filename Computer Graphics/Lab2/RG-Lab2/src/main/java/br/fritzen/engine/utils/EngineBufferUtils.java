package br.fritzen.engine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import br.fritzen.engine.model.Vertex;

public class EngineBufferUtils {

	public static FloatBuffer createVertexBuffer(Vertex[] vertices) {

		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);

		for (int i = 0; i < vertices.length; i++) {

			floatBuffer.put(vertices[i].getPosition().x);
			floatBuffer.put(vertices[i].getPosition().y);
			floatBuffer.put(vertices[i].getPosition().z);

			floatBuffer.put(vertices[i].getTexture().x);
			floatBuffer.put(vertices[i].getTexture().y);

			floatBuffer.put(vertices[i].getNormal().x);
			floatBuffer.put(vertices[i].getNormal().y);
			floatBuffer.put(vertices[i].getNormal().z);

			floatBuffer.put(vertices[i].getTangent().x);
			floatBuffer.put(vertices[i].getTangent().y);
			floatBuffer.put(vertices[i].getTangent().z);

		}

		floatBuffer.flip();

		return floatBuffer;
	}

	public static FloatBuffer createFloatPositionBuffer(ArrayList<Vector3f> vertices) {

		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(vertices.size() * 3);

		for (Vector3f v : vertices) {

			floatBuffer.put(v.x);
			floatBuffer.put(v.y);
			floatBuffer.put(v.z);

		}

		floatBuffer.flip();

		return floatBuffer;
	}

	public static IntBuffer createFlippedBuffer(int[] values) {

		IntBuffer buffer = BufferUtils.createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}

	public static int[] toIntArray(Integer[] data) {

		int[] res = new int[data.length];

		for (int i = 0; i < data.length; i++) {
			res[i] = data[i].intValue();
		}

		return res;
	}

	
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		
		ByteBuffer buffer;
		Path path = Paths.get(resource);

		if (Files.isReadable(path)) {
			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
				while (fc.read(buffer) != -1)
					;
			}
		} else {
			try (InputStream source = EngineBufferUtils.class.getResourceAsStream(resource);
					ReadableByteChannel rbc = Channels.newChannel(source)) {
				buffer = BufferUtils.createByteBuffer(bufferSize);

				while (true) {
					int bytes = rbc.read(buffer);
					if (bytes == -1) {
						break;
					}
					if (buffer.remaining() == 0) {
						buffer = resizeBuffer(buffer, buffer.capacity() * 2);
					}
				}
			}
		}

		buffer.flip();
		return buffer;
	}

	
	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	
	public static FloatBuffer createFlippedBuffer(Vector2f[] vertices) {
		
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(vertices.length * 2);

		for (Vector2f v : vertices) {

			floatBuffer.put(v.x);
			floatBuffer.put(v.y);

		}

		floatBuffer.flip();

		return floatBuffer;
		
	}

	public static FloatBuffer createFloatPositionBuffer(float[] vertices) {
		
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(vertices.length);

		for (Float f : vertices) {
			floatBuffer.put(f);
		}

		floatBuffer.flip();

		return floatBuffer;
	}
	
}
