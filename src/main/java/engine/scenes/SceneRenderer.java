package engine.scenes;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import engine.GameDisplay;
import engine.common.GameObject;
import engine.common.gameObjects.Camera;
import engine.graphics.SceneShaderProgram;
import engine.graphics.geometry.Mesh;
import engine.utils.math.Transformation;

/**
 * Our renderer, TODO: Abstract out to separate class to be inherited by
 * specific renderers
 * 
 * @author brandon.porter
 *
 */
public class SceneRenderer {
	private Scene _scene;
	private SceneShaderProgram _shaderProgram;
	private final Map<Mesh, LinkedList<GameObject>> _drawableEntities = new LinkedHashMap<>();

	/**
	 * Initializes the renderer
	 * 
	 * @param _scene
	 *            the scene holding this renderer
	 * @throws Exception
	 */
	public void init(Scene scene) throws Exception {
		this._scene = scene;
		this._shaderProgram = new SceneShaderProgram();
	}

	/**
	 * Adds a mesh to the scene rendering process
	 * 
	 * @param mesh
	 *            the mesh to render
	 * @param gameObject
	 *            the attached game object
	 */
	public void addMesh(Mesh mesh, GameObject gameObject) {
		// For better performance, we use the same mesh for many entities just
		// with different transformation properties
		LinkedList<GameObject> gameObjects = _drawableEntities.get(mesh);
		if (gameObjects == null) {
			gameObjects = new LinkedList<GameObject>();
			_drawableEntities.put(mesh, gameObjects);
		}

		// Adds the mesh's game object to the end of the list
		gameObjects.add(gameObject);
	}

	/**
	 * Begins the rendering process
	 * 
	 * @param graphics
	 *            the graphics controller for the current display
	 */
	protected void preRender() {
		// Clear the current frame before we render the next frame
		GameDisplay.getGraphicsController().clearGraphics();
		_shaderProgram.bind();
	}

	/**
	 * Renders the meshes to screen
	 */
	protected void render() {
		Camera camera = _scene.getCamera();

		// Draw each mesh
		for (Mesh mesh : _drawableEntities.keySet()) {
			_shaderProgram.setColor(mesh.getMaterial().getColor());
			_shaderProgram.setProjectionMatrix(camera.getProjectionMatrix());

			// Render each mesh with the specified game object transformation
			for (GameObject gameObject : _drawableEntities.get(mesh)) {
				_shaderProgram.setWorldViewMatrix(
						Transformation.buildWorldViewMatrix(gameObject.getTransform(), camera.getViewMatrix()));
				mesh.render();
			}
		}
	}

	/**
	 * Ends the rendering process
	 * 
	 * @param graphics
	 *            the graphics controller for the current display
	 */
	protected void endRender() {
		_shaderProgram.unbind();
	}

	/**
	 * Disposes the renderer
	 */
	protected void dispose() {
		if (_shaderProgram != null)
			_shaderProgram.dispose();
	}
}
