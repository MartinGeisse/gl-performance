/**
 * Copyright (c) 2010 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package glperf;

import glperf.system.LwjglNativeLibraryHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import java.nio.DoubleBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * The main class.
 */
public class GenericFunctionMain {

	public static final int NUM_QUADS = 100_000;

	/**
	 * The main method.
	 *
	 * @param args command-line arguments
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		Random random = new Random();

		// startup
		LwjglNativeLibraryHelper.prepareNativeLibraries();
		Display.setDisplayMode(new DisplayMode(800, 600));
		// Display.setFullscreen(true);
		Display.create(new PixelFormat(0, 24, 0));
		Keyboard.create();
		Keyboard.poll();

		// initialize GL state
		glMatrixMode(GL_PROJECTION);
		glOrtho(0, 800, 600, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glColor3f(1.0f, 0.0f, 0.0f);

		// define vertices
		DoubleBuffer vertexData = DoubleBuffer.allocate(6);
		vertexData.put(0);
		vertexData.put(0);
		vertexData.put(400);
		vertexData.put(0);
		vertexData.put(0);
		vertexData.put(300);
		vertexData.rewind();

		// initialize vertex buffer
		int vertexBufferName = 1;
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferName);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// initialize shaders
		int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vertexShader, "attribute vec2 coordinates; void main(void) { gl_Position = vec4(coordinates,0.0, 1.0); }");
		GL20.glCompileShader(vertexShader);
		int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fragmentShader, "void main(void) { gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0); }");
		GL20.glCompileShader(fragmentShader);
		int shaderProgram = GL20.glCreateProgram();
		GL20.glAttachShader(shaderProgram, vertexShader);
		GL20.glAttachShader(shaderProgram, fragmentShader);
		GL20.glLinkProgram(shaderProgram);
		GL20.glUseProgram(shaderProgram);

		// "Associate the shader programs to buffer objects" (I don't fully understand this yet)
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferName);
		int coordinatesLocation = GL20.glGetAttribLocation(shaderProgram, "coordinates");
		GL20.glVertexAttribPointer(coordinatesLocation, 2, GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(coordinatesLocation);

		// countdown
		for (int i = 3; i > 0; i--) {
			System.out.println("* " + i);
			Thread.sleep(1000);
		}

		// indicate start
		glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		glFlush();
		Display.update();

		// performance test
		for (int i = 0; i < NUM_QUADS; i++) {
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 4);
		}
		glFlush();
		Display.update();

		// keep alive
		while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Display.processMessages();
			Mouse.poll();
			Keyboard.poll();
		}

		// shutdown
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
		System.exit(0);

	}

}
