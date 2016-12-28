package engine.input;

import engine.Window;
import engine.utils.debug.Logger;

/**
 * Handles any keyboard/mouse/gamepad input for our game engine
 * 
 * @author brandon.porter
 *
 */
public class InputHandler {
	private static final Logger _log = new Logger("InputHandler");

	private final KeyboardInput _keyboard = new KeyboardInput();

	/**
	 * Constructs an input handler
	 */
	public InputHandler(Window window) {
		window.setKeyStateChangedCallback(this::onKeyStateChanged);
	}

	/**
	 * Gets the current keyboard input handler
	 * 
	 * @return keyboard
	 */
	public final KeyboardInput getKeyboard() {
		return _keyboard;
	}

	/**
	 * Callback from window for when a key state has changed (i.e. pressed,
	 * released)
	 * 
	 * @param keyValue
	 * @param keyState
	 * @param mods
	 */
	private void onKeyStateChanged(int keyValue, int keyState, int mods) {
		_log.debug("Key State Changed: Key: %d, State: %d, Mods: %d", keyValue, keyState, mods);
		_keyboard.keyStateChanged(keyValue, keyState, mods);
	}
}
