package com.gd.engine.graphics;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 * Controls the graphics for an OpenGL display
 * 
 * @author brandon.porter
 *
 */
public class OpenGLGraphicsController extends GraphicsController {

	/**
	 * Construct an OpenGL graphics controller
	 * 
	 * @param graphicsOptions
	 *            additional options to initialize the graphics
	 */
	public OpenGLGraphicsController(GraphicsOptions graphicsOptions) {
		super(graphicsOptions);
	}

	@Override
	public void init() {
		GL.createCapabilities();

		// Clear the screen
		clearColor(0, 0, 0, 0);

		// Enable depth test - this allows pixels that are far away
		// to be drawn first so our 3D objects do not look wack
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// Enables polygon mode, so we can see all the triangles that compose a
		// model
		// if (_opts.showTriangles) {
		// GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		// }

		// Support for transparencies
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void clearColor(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
	}
}
