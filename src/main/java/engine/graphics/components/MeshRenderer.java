package engine.graphics.components;

import engine.common.Component;
import engine.graphics.geometry.Mesh;

/**
 * Component that renders a mesh
 * 
 * @author Brandon Porter
 *
 */
public class MeshRenderer extends Component {
	private final Mesh _mesh;

	/**
	 * Constructs a mesh renderer with the default mesh
	 * 
	 * @param mesh
	 *            mesh to render
	 */
	public MeshRenderer(Mesh mesh) {
		super("Mesh Renderer");
		this._mesh = mesh;
	}

	/**
	 * @return the mesh being drawn for this mesh renderer
	 */
	public Mesh getMesh() {
		return _mesh;
	}

	/**
	 * Initializes the renderer by submitting it to the graphics renderer
	 */
	private void init() {
		this.getScene().getRenderer().addMesh(_mesh, this.getGameObject());
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
