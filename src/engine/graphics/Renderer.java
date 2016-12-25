package engine.graphics;

import org.joml.Vector3f;

import engine.GameDisplay;
import engine.game.objects.GameObject;
import engine.utils.debug.Logger;
import engine.utils.math.Transformation;

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
	 *            the visual window the user can see and interact with for the
	 *            game
	 */
	public void render(GameDisplay display, GameObject gameObject) {
		// Update viewport to window ONLY IF the window has been resized
		_log.debug("Rendering...");

		// Clear the current frame before we render the next frame
		display.getGraphicsController().clearGraphics();

		_sceneShaderProgram.bind();
		
		// Set uniforms
		_sceneShaderProgram.setColor(new Vector3f(1, 1, 1));
		_sceneShaderProgram.setProjectionMatrix(display.getCamera().getProjectionMatrix());
		_sceneShaderProgram.setWorldViewMatrix(Transformation.buildWorldViewMatrix(gameObject, display.getCamera()));
		
		// Render game object
		gameObject.getMesh().render();

		_sceneShaderProgram.unbind();

	}

	/**
	 * Disposes the renderer
	 */
	public void dispose() {
		if (_sceneShaderProgram != null)
			_sceneShaderProgram.dispose();
	}
}
