/**
 * Copyright (c) 2010 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package glperf;

import glperf.system.LwjglNativeLibraryHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * The main class.
 */
public class FixedFunctionMain {

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
			glBegin(GL_TRIANGLES);
			glVertex2d(0, 0);
			glVertex2d(400, 0);
			glVertex2d(0, 300);
			glEnd();
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
