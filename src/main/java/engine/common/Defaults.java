package engine.common;

import org.joml.Vector3f;

/**
 * Contains any default values to be consumed by the engine or app
 * 
 * @author Brandon Porter
 *
 */
public class Defaults {

	/*
	 * Prevent outside classes from creating an instance
	 */
	private Defaults() {
	}

	/**
	 * Defaults for the game engine
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Engine {
		/**
		 * The default max frames per second
		 */
		public static final int MAX_FPS = 60;

		/**
		 * The default max updates per second
		 */
		public static final int MAX_UPS = 60;

		/*
		 * Prevent outside classes from creating an instance
		 */
		private Engine() {
		}
	}

	/**
	 * Defaults pertaining to the camera view
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Camera {
		/**
		 * The field of view the camera can see
		 */
		public static final float FIELD_OF_VIEW = (float) Math.toRadians(70.0f);

		/**
		 * Starting clipping distance for camera
		 */
		public static final float FRUSTUM_NEAR = 0.01f;

		/**
		 * End clipping distance for camera
		 */
		public static final float FRUSTUM_FAR = 1000.0f;

		/*
		 * Prevent outside classes from creating an instance
		 */
		private Camera() {
		}
	}

	/**
	 * Defaults for how our graphics display on screen
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Graphics {
		/**
		 * Whether to display every model's vertex without applied texturing
		 */
		public static final boolean POLYGON_MODE = false;

		/*
		 * Prevent outside classes from creating an instance
		 */
		private Graphics() {
		}
	}

	/**
	 * Defaults for the game window
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Window {
		/**
		 * The default text displayed as the title on the window
		 */
		public static final String TITLE = "Golden Dragon - Game Engine";

		/**
		 * The default width of the game window
		 */
		public static final int WIDTH = 1280;

		/**
		 * The default height of the game window
		 */
		public static final int HEIGHT = 720;

		/**
		 * Whether the window can be resized by default
		 */
		public static final boolean RESIZABLE = false;

		/**
		 * Whether to limit the GPU to output frames as high as the refresh rate
		 * of the monitor. If enabled it prevents screen tearing but can also
		 * introduce minor input lag.
		 */
		public static final boolean VSYNC = true;

		/**
		 * Whether or not to display the FPS (frames per second) on the window
		 * title bar
		 */
		public static final boolean SHOW_FPS = false;

		/*
		 * Prevent outside classes from creating an instance
		 */
		private Window() {
		}
	}

	/**
	 * Defaults for lighting in a scene
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Lighting {

		/**
		 * The default brightness of any light in the scene
		 */
		public static final float LIGHT_INTENSITY = 1;

		/**
		 * The default color for any light in the scene
		 */
		public static final Vector3f LIGHT_COLOR = new Vector3f(1, 1, 1);

		/**
		 * The z-axis normalized position of where the directional light (i.e.
		 * the sun) will remain. Since we are doing directional light positioning
		 * based off of rotation this will be the "home-position" of the directional
		 * light if no rotation is applied. This should really only ever be 1 or -1.
		 */
		public static final int DIRECTIONAL_Z_POSITION = -1;
	}
}
