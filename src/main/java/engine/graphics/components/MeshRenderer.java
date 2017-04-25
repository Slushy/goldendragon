package engine.graphics.components;

import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;

import engine.common.Camera;
import engine.common.Component;
import engine.graphics.IRenderer;
import engine.graphics.Material;
import engine.graphics.MaterialPropertyBlock;
import engine.graphics.UniformData;
import engine.graphics.UniformType;
import engine.graphics.geometry.Mesh;
import engine.graphics.geometry.Texture;
import engine.utils.Debug;
import engine.utils.math.Transformation;

/**
 * Component that renders a mesh
 * 
 * @author Brandon Porter
 *
 */
public class MeshRenderer extends Component implements IRenderer {
	private final Mesh _mesh;
	private final Material _material;
	private final MaterialPropertyBlock _propertyOverrides;

	/**
	 * Constructs a mesh renderer for a mesh and specific material
	 * 
	 * @param mesh
	 *            mesh to render
	 * @param material
	 *            material to render the mesh
	 */
	public MeshRenderer(Mesh mesh, Material material) {
		this(mesh, material, new MaterialPropertyBlock());
	}

	/**
	 * Constructs a mesh renderer for a mesh and specific material
	 * 
	 * @param mesh
	 *            mesh to render
	 * @param material
	 *            material to render the mesh
	 * @param materialPropertyOverrides
	 *            specific material property overrides for this renderer
	 */
	public MeshRenderer(Mesh mesh, Material material, MaterialPropertyBlock materialPropertyOverrides) {
		super("Mesh Renderer");
		this._mesh = mesh;
		this._material = material;
		this._propertyOverrides = materialPropertyOverrides;
	}

	/**
	 * @return the mesh being drawn for this mesh renderer
	 */
	public Mesh getMesh() {
		return _mesh;
	}

	/**
	 * @return the material being used by the mesh renderer
	 */
	@Override
	public Material getMaterial() {
		return _material;
	}

	/**
	 * @return the material properties only affecting this renderer instance
	 */
	@Override
	public MaterialPropertyBlock getProperties() {
		return _propertyOverrides;
	}

	/**
	 * Compares the mesh renderer for equality against the current mesh renderer
	 * 
	 * @param renderer
	 *            the mesh renderer to compare
	 * @return true if they are equal in instance and material and material
	 *         property overrides
	 */
	@Override
	public boolean compare(IRenderer renderer) {
		if (this == renderer)
			return true;

		return _material.compare(renderer.getMaterial()) && _propertyOverrides.compare(renderer.getProperties());
	}

	/**
	 * Called when time to render the mesh by the scene
	 *
	 * INTERNAL USE ONLY
	 * 
	 * @param transformation
	 * @param camera
	 * @param uniformData
	 */
	@Override
	public void render(Transformation transformation, Camera camera, UniformData uniformData) {
		if (!_mesh.isLoaded()) {
			Debug.error("Trying to render a mesh that isn't loaded yet: " + _mesh.getName());
			return;
		}

		// Set the model view matrix for this game object
		Matrix4fc modelViewMatrix = transformation.buildWorldViewMatrix(getGameObject().getTransform(),
				camera.getViewMatrix());
		uniformData.set(UniformType.WORLD_VIEW_MATRIX, modelViewMatrix);

		// Set material uniform properties with any renderer overrides
		MaterialPropertyBlock matProps = _material.getProperties();
		uniformData.set(UniformType.COLOR, _propertyOverrides.getColorOr(matProps.getColor()));
		uniformData.set(UniformType.SPECULAR_COLOR, _propertyOverrides.getColorOr(matProps.getColor()));
		uniformData.set(UniformType.SHININESS, _propertyOverrides.getShininessOr(matProps.getShininess()));
		// Textures
		Texture mainTexture = _propertyOverrides.getMainTextureOr(matProps.getMainTexture());
		uniformData.setTexture(UniformType.MAIN_TEXTURE, mainTexture, 0);
		uniformData.set(UniformType.USE_TEXTURE, mainTexture != null);

		// Bind VAO
		_mesh.getVAO().use();

		// Draw game object
		GL11.glDrawElements(GL11.GL_TRIANGLES, _mesh.getRenderableVertexCount(), GL11.GL_UNSIGNED_INT, 0);

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
