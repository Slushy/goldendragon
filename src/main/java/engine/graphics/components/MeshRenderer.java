package engine.graphics.components;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import engine.common.Component;
import engine.graphics.StandardShaderProgram;
import engine.graphics.geometry.Material;
import engine.graphics.geometry.Mesh;
import engine.graphics.geometry.VAO;
import engine.utils.Debug;

/**
 * Component that renders a mesh
 * 
 * @author Brandon Porter
 *
 */
public class MeshRenderer extends Component {
	private final Mesh _mesh;
	private final Material _material;

	/**
	 * Constructs a mesh renderer for a mesh and specific material
	 * 
	 * @param mesh
	 *            mesh to render
	 * @param material
	 *            material to render the mesh
	 */
	public MeshRenderer(Mesh mesh, Material material) {
		super("Mesh Renderer");
		this._mesh = mesh;
		this._material = material;
	}

	/**
	 * @return the mesh being drawn for this mesh renderer
	 */
	public Mesh getMesh() {
		return _mesh;
	}

	/**
	 * @return a clone of the material being used by the mesh renderer
	 */
	public Material getCopyOfMaterial() {
		return new Material(_material);
	}

	/**
	 * @return the material being used by the mesh renderer
	 */
	public Material getMaterial() {
		return _material;
	}

	/**
	 * Add the mesh to the scene renderer
	 */
	@SuppressWarnings("unused")
	private void init() {
		this.getScene().getRenderer().rendererAddedToScene(this);
	}

	/**
	 * Called when time to render the mesh by the scene
	 * 
	 * @param shaderProgram
	 */
	public void render(StandardShaderProgram shaderProgram) {
		if (!_mesh.isLoaded()) {
			Debug.error("Trying to render a mesh that isn't loaded yet: " + _mesh.getFileName());
			return;
		}
		// Bind VAO
		_mesh.getVAO().use();

		// Draw game object
		GL11.glDrawElements(GL11.GL_TRIANGLES, _mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		// Unbind
		_mesh.getVAO().done();
	}

	/**
	 * Disposes the renderer
	 */
	@Override
	protected void onDispose() {
		super.onDispose();
	}
}
