package engine.utils.inputs;

import engine.Window;

/**
 * Handles any keyboard/mouse/gamepad input for our game engine
 * 
 * @author brandon.porter
 *
 */
public class InputHandler {

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
		_keyboard.keyStateChanged(keyValue, keyState, mods);
	}
}
