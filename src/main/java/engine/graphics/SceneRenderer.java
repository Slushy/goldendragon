package engine.graphics;

import org.joml.Vector3f;

import engine.GraphicsController;
import engine.common.GameObject;
import engine.common.gameObjects.Camera;
import engine.graphics.geometry.Mesh;
import engine.scenes.Scene;
import engine.utils.Logger;
import engine.utils.math.Transformation;

/**
 * Our renderer, TODO: Abstract out to separate class to be inherited by
 * specific renderers
 * 
 * @author brandon.porter
 *
 */
public class SceneRenderer {
	private static final Logger _log = new Logger("SceneRenderer");

	private Scene _scene;
	private SceneShaderProgram _sceneShaderProgram;

	/**
	 * Initializes the renderer
	 * 
	 * @param _scene
	 *            the scene holding this renderer
	 * @throws Exception
	 */
	public void init(Scene scene) throws Exception {
		this._scene = scene;
		this._sceneShaderProgram = new SceneShaderProgram();
	}

	/**
	 * Begins the rendering process
	 * 
	 * @param graphics
	 *            the graphics controller for the current display
	 */
	public void preRender(GraphicsController graphics) {
		// Update viewport to window ONLY IF the window has been resized
		_log.debug("Rendering...");

		// Clear the current frame before we render the next frame
		graphics.clearGraphics();

		_sceneShaderProgram.bind();
	}

	/**
	 * Ends the rendering process
	 * 
	 * @param graphics
	 *            the graphics controller for the current display
	 */
	public void endRender(GraphicsController graphics) {
		_sceneShaderProgram.unbind();
	}

	/**
	 * Renders the mesh to the screen
	 * 
	 * @param mesh
	 */
	public void drawMesh(Mesh mesh, GameObject gameObject) {
		Camera camera = _scene.getCamera();

		// Set uniforms
		_sceneShaderProgram.setColor(new Vector3f(1, 1, 1));
		_sceneShaderProgram.setProjectionMatrix(camera.getProjectionMatrix());
		_sceneShaderProgram.setWorldViewMatrix(Transformation.buildWorldViewMatrix(gameObject.getTransform(), camera.getViewMatrix()));

		// Render game object
		mesh.render();
	}
	
	/**
	 * Disposes the renderer
	 */
	public void dispose() {
		if (_sceneShaderProgram != null)
			_sceneShaderProgram.dispose();
	}
}
