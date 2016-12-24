package engine.graphics;

import engine.GameDisplay;
import engine.utils.debug.Logger;

/**
 * Our renderer, TODO: Abstract out to separate class to be inherited by
 * specific renderers
 * 
 * @author brandon.porter
 *
 */
public class Renderer {
	private static final Logger _log = new Logger("Renderer");

	private SceneShaderProgram _sceneShaderProgram;

	/**
	 * Initializes the renderer
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		this._sceneShaderProgram = new SceneShaderProgram();
	}

	/**
	 * Renders the next frame to the screen
	 * 
	 * @param display
	 *            the visual window the user can see and interact with for the game
	 */
	public void render(GameDisplay display) {
		_log.debug("Rendering...");
		// Clear the current frame before we render the next frame
		display.getGraphicsController().clearGraphics();
	}

	/**
	 * Disposes the renderer
	 */
	public void dispose() {
		if (_sceneShaderProgram != null)
			_sceneShaderProgram.dispose();
	}
}
