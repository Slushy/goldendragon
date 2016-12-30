package engine;

/**
 * Defines default values to be consumed by the game engine
 * 
 * @author brandon.porter
 *
 */
public class EngineDefaults {
	/**
	 * The field of view the camera can see
	 */
	public static final float FIELD_OF_VIEW = (float) Math.toRadians(70.0f);

	/**
	 * Starting clipping distance for camera
	 */
	public static final float Z_NEAR = 0.01f;

	/**
	 * End clipping distance for camera
	 */
	public static final float Z_FAR = 1000.0f;

	/**
	 * The default max frames per second
	 */
	public static final int MAX_FPS = 60;

	/**
	 * The default max updates per second
	 */
	public static final int MAX_UPS = 30;
	
	/**
	 * Whether to display every model's vertex without applied texturing
	 */
	public static final boolean POLYGON_MODE = false;

	/*
	 * Prevent outside classes from creating an instance
	 */
	private EngineDefaults() {
	}

}
