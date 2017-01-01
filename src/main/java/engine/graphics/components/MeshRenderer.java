package engine.graphics.components;

import engine.common.Component;
import engine.graphics.SceneRenderer;
import engine.graphics.geometry.Mesh;

/**
 * Component that renders a mesh
 * 
 * @author Brandon Porter
 *
 */
public class MeshRenderer extends Component {
	private Mesh _mesh = null;

	/**
	 * Constructs an empty mesh renderer
	 */
	public MeshRenderer() {
		super("Mesh Renderer");
	}

	/**
	 * Constructs a mesh renderer with the default mesh
	 * 
	 * @param mesh
	 *            mesh to render
	 */
	public MeshRenderer(Mesh mesh) {
		this();
		this._mesh = mesh;
	}

	/**
	 * Renders the current game object mesh
	 * 
	 * @param sceneRenderer
	 */
	@Override
	public void render(SceneRenderer sceneRenderer) {
		if (_mesh == null)
			return;

		sceneRenderer.drawMesh(_mesh, this.getGameObject());
	}

	/**
	 * @return the mesh being drawn for this mesh renderer
	 */
	public Mesh getMesh() {
		return _mesh;
	}

	/**
	 * Disposes the attached mesh
	 */
	@Override
	protected void onDispose() {
		if (_mesh != null)
			_mesh.dispose();

		super.onDispose();
	}
}
