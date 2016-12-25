package engine.game.objects;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import engine.graphics.geo.Mesh;

/**
 * Our base game object represents a renderable item in the game
 * 
 * @author brandon.porter
 *
 */
public class GameObject extends Entity {
	private Mesh _mesh;
	private float _scale = 1.0f;

	/**
	 * Constructs a new game object
	 */
	public GameObject() {
		super(new Vector3f(), new Quaternionf());
	}

	/**
	 * Constructs a new game object
	 * 
	 * @param mesh
	 *            geometric mesh representing this game object
	 */
	public GameObject(Mesh mesh) {
		this();
		this._mesh = mesh;
	}

	/**
	 * Gets the scale of this game object
	 * 
	 * @return current scale
	 */
	public float getScale() {
		return _scale;
	}

	/**
	 * Sets the scale for this game object
	 * 
	 * @param scale
	 *            [0 - infinity] deals with the size of this object
	 */
	public void setScale(float scale) {
		this._scale = scale;
	}

	/**
	 * Gets the current mesh for this game object
	 * 
	 * @return
	 */
	public Mesh getMesh() {
		return _mesh;
	}

	/**
	 * Sets the mesh for this game object
	 * 
	 * @param mesh
	 *            geometric mesh representing this game object
	 */
	public void setMesh(Mesh mesh) {
		this._mesh = mesh;
	}

	/**
	 * Disposes all meshes associated to this game object
	 */
	public void dispose() {
		if (_mesh != null)
			_mesh.dispose();
	}
}
