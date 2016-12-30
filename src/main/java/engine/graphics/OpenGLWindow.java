package engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import engine.input.Key;
import engine.utils.Logger;

/**
 * An OpenGL window that the user sees when interacting with the game
 * 
 * @author brandon.porter
 *
 */
public class OpenGLWindow extends Window {
	private static final Logger _log = new Logger("OpenGLWindow", Logger.LoggerLevel.DEBUG);

	// Store references to callbacks to prevent java from doing any
	// garbage collection on them while they are still in use
	private GLFWErrorCallback _errorCallback;
	private GLFWKeyCallback _keyCallback;
	private GLFWWindowSizeCallback _windowSizeCallback;
	
	/**
	 * Constructs an OpenGL window
	 * 
	 * @param title
	 *            text displayed on the game window
	 * @param width
	 *            starting width of the game window
	 * @param height
	 *            starting height of the game window
	 * @param options
	 *            additional options to initialize the window
	 */
	public OpenGLWindow(String title, int width, int height, WindowOptions windowOptions) {
		super(title, width, height, windowOptions);
	}

	@Override
	public void show() {
		glfwShowWindow(getWindowId());
	}

	@Override
	public void hide() {
		glfwHideWindow(getWindowId());
	}

	@Override
	public void render() {
		// TODO: More Information
		glfwSwapBuffers(getWindowId());
		glfwPollEvents();
	}

	@Override
	public boolean shouldClose() {
		return glfwWindowShouldClose(getWindowId());
	}

	@Override
	public void close() {
		glfwSetWindowShouldClose(getWindowId(), true);
	}

	@Override
	public void dispose() {
		long windowId = getWindowId();
		if (windowId != NULL) {
			glfwDestroyWindow(windowId);
		}
	}

	@Override
	protected void updateWindowTitle(String title) {
		glfwSetWindowTitle(getWindowId(), title);
	}

	@Override
	protected long createWindow(String title, int width, int height) {
		// Initialize GLFW
		initGLFW();

		// Creates the window
		long windowId = glfwCreateWindow(width, height, title, NULL, NULL);
		if (windowId == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}

		// Create window callbacks
		setupCallbacks(windowId);

		// Get the resolution of the primary monitor and center our window
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(windowId, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

		// Attach the graphics context to the window
		glfwMakeContextCurrent(windowId);

		// Check and update our size scale if the window coordinates on the OS
		// are not the
		// same resolution as how we represent our window size
		checkIfWindowCoordinatesDifferFromPixels(windowId, width, height);

		// Set V-Sync for window if enabled
		if (isVSync()) {
			// note: 1 is for full frame rate, 2 is for half, etc.
			glfwSwapInterval(1);
		}

		// Make the window visible
		return windowId;
	}

	/*
	 * Initializes GLFW for OpenGL so we can create display our window
	 */
	private void initGLFW() {
		// Setup an error callback. An error from GLFW will print
		// the error message in System.err
		glfwSetErrorCallback(this._errorCallback = GLFWErrorCallback.createPrint(System.err));

		// Initialize GLFW - most GLFW functions will not work before doing this
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		// Optional, the current window hints are already the default
		// TODO: More information
		glfwDefaultWindowHints();

		// Override the important window hints
		// TODO: More information
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, this.windowOptions.resizable ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		if (System.getProperty("os.name").contains("Mac")) {
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
			glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		} else {
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
		}
	}

	/*
	 * Creates any additional callbacks on the window
	 */
	private void setupCallbacks(long windowId) {
		// Resize callback - gets fired when this window is resized
		glfwSetWindowSizeCallback(windowId,
				this._windowSizeCallback = GLFWWindowSizeCallback.create((long window, int width, int height) -> {
					this.width = width;
					this.height = height;
					this.resized = true;
					if (this.onWindowResizedCallback != null)
						this.onWindowResizedCallback.run();
				}));
		
		// Key press callback - gets fired when a key is pressed
		glfwSetKeyCallback(windowId,
				this._keyCallback = GLFWKeyCallback.create((window, key, scancode, action, mods) -> {
					if (this.onWindowKeyStateChanged != null) {
						this.onWindowKeyStateChanged.apply(key, action, mods);
					}
				}));
	}

	/*
	 * Sets the size scale to a number if the ACTUAL window size is different
	 * then what the size is in pixels. This is a problem for macs with retina
	 * resolution that for example may show a 1280px width as 2560px
	 */
	private void checkIfWindowCoordinatesDifferFromPixels(long windowId, int setWidth, int setHeight) {
		int[] actualWidth = new int[1];
		int[] actualHeight = new int[1];
		glfwGetFramebufferSize(windowId, actualWidth, actualHeight);

		// Set a scale that we can use to get the actual width/height
		if (actualWidth[0] != setWidth && setWidth != 0) {
			this.sizeScale = actualWidth[0] / setWidth;
			_log.warn("OS window resolution is different by a scale of %.2f", sizeScale);
		}
	}
}
