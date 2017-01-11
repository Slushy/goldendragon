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
		public static final float FIELD_OF_VIEW = (float) Math.toRadians(60.0f);

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
	 * Defaults related to a game scene
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Scene {

		/**
		 * Any object being added to the scene will by default have this facing
		 * direction. This will be useful in determining direction vectors from
		 * rotations
		 */
		public static final Vector3f OBJECT_FACING_DIRECTION = new Vector3f(0, 0, -1);

		/*
		 * Prevent outside classes from creating an instance
		 */
		private Scene() {
		}
	}

	/**
	 * Defaults for materials
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Materials {
		/**
		 * The default color of a non-textured material is White (255, 255, 255)
		 */
		public static final Vector3f COLOR = new Vector3f(1.0f, 1.0f, 1.0f);

		/**
		 * The default color of the material shininess when the light is shining
		 * into it is black (which shows up as no reflectance since the final
		 * specular calculated value is multiplied against the specular color)
		 */
		public static final Vector3f SPECULAR_COLOR = new Vector3f(0.0f, 0.0f, 0.0f);

		/**
		 * The min the shininess factor for a material can be
		 */
		public static final float SHININESS_MIN = 0.01f;

		/**
		 * The max the shininess factor for a material can be
		 */
		public static final float SHININESS_MAX = 1.0f;

		/*
		 * Prevent outside classes from creating an instance
		 */
		private Materials() {

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
		 * The default range for any point light (the point light's range is
		 * also known as the distance traveled from the middle of the light
		 * source before being fully diminished)
		 */
		public static final float POINT_LIGHT_RANGE = 10;

		/**
		 * The default cone angle for any spotlight. Determines the size of the
		 * light cone emitted from the spotlight light source
		 */
		public static final float SPOTLIGHT_ANGLE = 30;

		/**
		 * The attenuation constant is represented as the variable 'a' in the
		 * following equation (function of light intensity over distance):
		 * 
		 * att(a, b) = 1 / (a + b*r*r)
		 */
		public static final float ATTENUATION_CONSTANT = 1;

		/**
		 * The attenuation quadratic constant is represented as the variable 'b'
		 * in the following equation (function of light intensity over
		 * distance):
		 * 
		 * att(a, b) = 1 / (a + b*r*r)
		 */
		public static final float ATTENUATION_QUADRATIC = 25f;

		/**
		 * The max amount of point lights that can affect a given game object
		 * each render cycle.
		 * 
		 * NOTE: THIS SHOULD MATCH THE SAME CONSTANT "MAX_LIGHTS" DEFINED IN THE
		 * SHADER FILE
		 */
		public static final int MAX_RENDERED_POINT_LIGHTS_PER_OBJECT = 4;
	}
}
