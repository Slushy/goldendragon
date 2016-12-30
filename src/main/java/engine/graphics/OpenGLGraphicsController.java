package engine.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import engine.utils.Logger;

/**
 * Controls the graphics for an OpenGL display
 * 
 * @author brandon.porter
 *
 */
public class OpenGLGraphicsController extends GraphicsController {
	private static final Logger _log = new Logger("OpenGLGraphicsController", Logger.LoggerLevel.DEBUG);
	
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
		// Must be done first thing before any OpenGL function is called
		GL.createCapabilities();

		// Clear the screen
		clearColor(0, 0, 0, 0);

		// Enable depth test - this allows pixels that are far away
		// to be drawn first so our 3D objects do not look wack
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// Enabling polygon mode shows all the lines that compose a
		// model
		setPolygonMode(graphicsOptions.polygonMode);

		// Support for transparencies
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void clearColor(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
	}

	@Override
	public void clearGraphics() {
		clearGraphics(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void clearGraphics(int bits) {
		GL11.glClear(bits);
	}

	@Override
	public void setViewport(int x, int y, int width, int height) {
		_log.debug("Setting viewport to x: %d, y: %d, width: %d, height: %d", x, y, width, height);
		GL11.glViewport(x, y, width, height);
	}

	@Override
	public void setPolygonMode(boolean polygonMode) {
		_log.debug("Setting polygonMode to %b", polygonMode);
		int glMode = polygonMode ? GL11.GL_LINE : GL11.GL_FILL;
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, glMode);
		
		super.setPolygonMode(polygonMode);
	}
}
