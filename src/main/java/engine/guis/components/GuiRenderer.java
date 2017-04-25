package engine.guis.components;

import engine.common.Camera;
import engine.graphics.IRenderer;
import engine.graphics.Material;
import engine.graphics.MaterialPropertyBlock;
import engine.graphics.UniformData;
import engine.guis.GuiComponent;
import engine.utils.Debug;
import engine.utils.math.Transformation;

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
