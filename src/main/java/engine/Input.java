package engine;

import engine.input.InputHandler;
import engine.input.Key;

/**
 * Public API to handle/listen to input-related events
 * 
 * @author Brandon Porter
 *
 */
public final class Input {
	private static InputHandler _inputHandler;

	// Static class
	private Input() {
	}

	/**
	 * Called once per frame, this is where we will store
	 * and set the frame specific input states
	 */
	protected static void updateInputState() {
		// TODO: IMPLEMENT ME
	}
	
	/**
	 * Checks if the passed in key was pressed this frame
	 * 
	 * @param key
	 *            the key to check
	 * @return true or false if key was pressed
	 */
	public static boolean keyPressed(Key key) {
		return _inputHandler.getKeyboard().keyPressed(key);
	}

	/**
	 * Checks if the passed in key is either pressed or being held
	 * 
	 * @param key
	 *            the key to check
	 * @return true or false if key is down
	 */
	public static boolean keyDown(Key key) {
		return _inputHandler.getKeyboard().keyDown(key);
	}

	/**
	 * Checks if the passed in key was released this frame
	 * 
	 * @param key
	 *            the key to check
	 * @return true or false if key was released
	 */
	public static boolean keyReleased(Key key) {
		return _inputHandler.getKeyboard().keyReleased(key);
	}

	/**
	 * Initializes the input handler
	 * 
	 * @param window
	 *            the game window
	 */
	protected static void init(Window window) {
		_inputHandler = new InputHandler(window);
	}
}
