package engine.rendering;

import engine.rendering.material.Material;
import engine.rendering.material.MaterialPropertyBlock;
import engine.rendering.shader.UniformData;
import engine.scene.GuiComponent;
import engine.scene.Transformation;
import engine.scene.gameobjects.Camera;
import engine.utils.Debug;

/**
 * A GuiRenderer is a component that renders GUI game objects
 * 
 * @author Brandon Porter
 *
 */
public class GuiRenderer extends GuiComponent implements IRenderer {
	private static final String COMPONENT_NAME = "GUI Renderer";

	/**
	 * Constructs a GuiRenderer
	 */
	public GuiRenderer() {
		super(COMPONENT_NAME);
	}

	@Override
	public void render(Transformation transformation, Camera camera, UniformData uniformData) {
		Debug.log("Rendering a GUI component and such");
		
	}

	@Override
	public Material getMaterial() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MaterialPropertyBlock getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean compare(IRenderer renderer) {
		// TODO Auto-generated method stub
		return false;
	}

}
