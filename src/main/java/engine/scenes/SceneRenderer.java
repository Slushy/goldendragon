package engine.scenes;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import engine.GameDisplay;
import engine.common.gameObjects.Camera;
import engine.graphics.StandardShaderProgram;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Material;
import engine.utils.Debug;
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
	private StandardShaderProgram _shaderProgram;

	private final Map<Long, LinkedList<Long>> _meshMaterials = new LinkedHashMap<>();
	private final Map<Long, LinkedList<MeshRenderer>> _materialRenderers = new LinkedHashMap<>();

	/**
	 * Initializes the renderer
	 * 
	 * @param _scene
	 *            the scene holding this renderer
	 * @throws Exception
	 */
	public void init(Scene scene) throws Exception {
		this._scene = scene;
		this._shaderProgram = StandardShaderProgram.Instance;
	}

	/**
	 * Adds a mesh to the scene rendering process
	 * 
	 * @param mesh
	 *            the mesh to render
	 * @param gameObject
	 *            the attached game object
	 */
	// public void addMesh(Mesh mesh, GameObject gameObject) {
	// // For better performance, we use the same mesh for many entities just
	// // with different transformation properties
	// LinkedList<GameObject> gameObjects = _drawableEntities.get(mesh);
	// if (gameObjects == null) {
	// gameObjects = new LinkedList<GameObject>();
	// _drawableEntities.put(mesh, gameObjects);
	// }
	//
	// // Adds the mesh's game object to the end of the list
	// gameObjects.add(gameObject);
	// }

	/**
	 * Adds renderer to scene
	 * 
	 * @param renderer
	 */
	public void rendererAddedToScene(MeshRenderer renderer) {
		long meshId = renderer.getMesh().getInstanceId();
		long matId = renderer.getMaterial().getInstanceId();

		// Check if mesh exists
		LinkedList<Long> materials = _meshMaterials.get(meshId);
		if (materials == null) {
			materials = new LinkedList<>();
			_meshMaterials.put(meshId, materials);
		}

		// Check if material exists
		LinkedList<MeshRenderer> renderers = _materialRenderers.get(matId);
		if (renderers == null) {
			materials.add(matId);
			renderers = new LinkedList<>();
			_materialRenderers.put(matId, renderers);
		}

		// Add Renderer to list
		renderers.add(renderer);
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
		_shaderProgram.setProjectionMatrix(camera.getProjectionMatrix());

		// For each similar mesh
		for (long meshId : _meshMaterials.keySet()) {
			// For each similar material
			for (long matId : _meshMaterials.get(meshId)) {
				// For each renderer with the shared mesh & material
				Material mat = _materialRenderers.get(matId).peekFirst().getMaterial();
				mat.renderStart(_shaderProgram);
				for (MeshRenderer renderer : _materialRenderers.get(matId)) {
					// Set the transformation matrix
					_shaderProgram.setWorldViewMatrix(Transformation
							.buildWorldViewMatrix(renderer.getGameObject().getTransform(), camera.getViewMatrix()));
					// Tell the renderer to render
					renderer.render(_shaderProgram);
				}
				mat.renderEnd();
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
