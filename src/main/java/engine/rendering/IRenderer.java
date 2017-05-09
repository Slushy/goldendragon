package engine.rendering;

import engine.rendering.material.Material;
import engine.rendering.material.MaterialPropertyBlock;
import engine.rendering.shader.UniformData;
import engine.scene.Transformation;
import engine.scene.gameobjects.Camera;

/**
 * Interface that represents the blueprint for a renderer component. A renderer
 * controls how an entity is drawn in the scene.
 * 
 * @author Brandon Porter
 *
 */
public interface IRenderer {
	/**
	 * Called when time to render the an entity by the scene
	 *
	 * INTERNAL USE ONLY
	 * 
	 * @param transformation
	 * @param camera
	 * @param uniformData
	 */
	public void render(Transformation transformation, Camera camera, UniformData uniformData);

	/**
	 * @return the material being used by the mesh renderer
	 */
	public Material getMaterial();

	/**
	 * @return the material properties only affecting this renderer instance
	 */
	public MaterialPropertyBlock getProperties();

	/**
	 * Compares the renderer for equality against the current renderer
	 * 
	 * @param renderer
	 * @return boolean of whether the two renderers are equal
	 */
	public boolean compare(IRenderer renderer);
}
