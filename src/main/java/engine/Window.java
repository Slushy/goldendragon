package engine;

import engine.common.Defaults;

/**
 * Base implementation of all windows
 * 
 * @author brandon.porter
 *
 */
public abstract class Window {
	protected final WindowOptions windowOptions;
	protected float sizeScale = 1;

	protected String title;
	protected int width;
	protected int height;
	protected boolean resized;

	protected WindowKeyCallback onWindowKeyStateChanged = null;
	protected Runnable onWindowResizedCallback = null;

	private long _id;

	/**
	 * Constructs a window
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
	public Window(String title, int width, int height, WindowOptions options) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.windowOptions = options;
	}

	/**
	 * Initializes the window
	 */
	public void init() {
		this._id = createWindow(title, width, height);
	}

	/**
	 * Gets the unique id of the window
	 * 
	 * @return window Id
	 */
	public final long getWindowId() {
		return _id;
	}

	/**
	 * Checks if the window has been resized recently
	 * 
	 * @return
	 */
	public boolean hasResized() {
		return resized;
	}

	/**
	 * Sets the resized boolean
	 * 
	 * @param resized
	 *            true or false
	 */
	public void setResized(boolean resized) {
		this.resized = resized;
	}

	/**
	 * Gets the window title
	 * 
	 * @return text displayed on the game window
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the window title
	 * 
	 * @param title
	 *            text displayed on the game window
	 */
	public void setTitle(String title) {
		this.title = title;
		this.updateWindowTitle(title);
	}

	/**
	 * Sets a callback for when the window is resized
	 * 
	 * @param callback
	 *            function that accepts and returns void
	 */
	public void setWindowResizedCallback(Runnable callback) {
		this.onWindowResizedCallback = callback;
	}

	/**
	 * Sets a callback for when a key on the window has a state change (i.e.
	 * pressed, released)
	 * 
	 * @param callback
	 *            function that accepts keycode, keystate, mods
	 */
	public void setKeyStateChangedCallback(WindowKeyCallback callback) {
		this.onWindowKeyStateChanged = callback;
	}

	/**
	 * Checks if the window is enabled for vSync
	 * 
	 * @return true or false if the window is vSync enabled
	 */
	public boolean isVSync() {
		return windowOptions.vSync;
	}

	/**
	 * Gets the window width
	 * 
	 * @return windows width in pixels
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the scaled version of the window width. This will be different from
	 * normal width on systems where window coordinates are not the same
	 * resolution as how we represent our window size. This should be used to
	 * update viewport correctly.
	 * 
	 * @return scaled width of the window
	 */
	public int getWidthScaled() {
		return (int) (getWidth() * sizeScale);
	}

	/**
	 * Gets the window height
	 * 
	 * @return windows height in pixels
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the scaled version of the window height. This will be different from
	 * normal height on systems where window coordinates are not the same
	 * resolution as how we represent our window size. This should be used to
	 * update viewport correctly.
	 * 
	 * @return scaled height of the window
	 */
	public int getHeightScaled() {
		return (int) (getHeight() * sizeScale);
	}

	/**
	 * Closes the current window
	 */
	public abstract void close();

	/**
	 * Determines if the window should close
	 * 
	 * @return true or false if the window should close
	 */
	public abstract boolean shouldClose();

	/**
	 * Renders the window, continually called from the game loop several times a
	 * second
	 */
	public abstract void render();

	/**
	 * Makes the window visible on screen
	 */
	public abstract void show();

	/**
	 * Makes the window invisible on screen
	 */
	public abstract void hide();

	/**
	 * Cleans up and destroys the window instance
	 */
	public abstract void dispose();

	/**
	 * Creates an instance of an implementation specific window
	 * 
	 * @param title
	 *            text displayed on the window title
	 * @param width
	 *            width of the window in pixels
	 * @param height
	 *            height of the window in pixels
	 * @return ID of the new window
	 */
	protected abstract long createWindow(String title, int width, int height);

	/**
	 * Update the window title
	 * 
	 * @param title
	 *            updated title of the window
	 */
	protected abstract void updateWindowTitle(String title);

	/**
	 * Additional options to initialize a window
	 * 
	 * @author brandon.porter
	 *
	 */
	public static class WindowOptions {
		/**
		 * Whether the window is resizable
		 */
		public boolean resizable = Defaults.Window.RESIZABLE;

		/**
		 * Whether to limit the GPU to output frames as high as the refresh rate
		 * of the monitor. If enabled it prevents screen tearing but can also
		 * introduce minor input lag.
		 */
		public boolean vSync = Defaults.Window.VSYNC;
	}

	@FunctionalInterface
	public static interface WindowKeyCallback {
		public void apply(int keyCode, int keyState, int mods);
	}
}
