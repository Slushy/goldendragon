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
	 * Add the mesh to the scene renderer
	 */
	@SuppressWarnings("unused")
	private void init() {
		this.getScene().getRenderer().addMesh(_mesh, this.getGameObject());
	}

	/**
	 * Disposes the attached mesh
	 */
	@Override
	protected void onDispose() {
		super.onDispose();
	}
}
