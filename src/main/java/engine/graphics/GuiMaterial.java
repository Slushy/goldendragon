package engine.graphics;

/**
 * A GUI material is pretty straightforward and is really only used to represent
 * the GUI shader
 * 
 * @author Brandon Porter
 *
 */
public class GuiMaterial extends Material {
	public static final GuiMaterial DEFAULT = new GuiMaterial();
	private static final String ENTITY_NAME = "GUI Material";

	/**
	 * Constructs a new GUI material with the GUI shader
	 */
	public GuiMaterial() {
		super(ENTITY_NAME, ShaderType.GUI);
	}

	/**
	 * Copy Constructor to construct a new material from an existing one
	 * 
	 * @param material
	 *            material to copy
	 */
	public GuiMaterial(GuiMaterial material) {
		super(material);
	}

	/**
	 * Compares the material for equality against the current material
	 * 
	 * @param mat
	 *            the material to compare
	 * @return true if they are equal in instance
	 */
	@Override
	public boolean compare(Material mat) {
		return super.compare(mat) && mat instanceof GuiMaterial;
	}
}
