package engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

import java.util.HashMap;
import java.util.Map;

import engine.utils.Logger;

/**
 * Keeps track of our keyboard keys and their current states
 * 
 * @author brandon.porter
 *
 */
public class KeyboardInput {
	private static final Logger _log = new Logger("KeyboardInput");

	private final Map<Integer, KeyState> _keyStates = new HashMap<>(Key.size());

	/**
	 * Constructs a new keyboard
	 */
	public KeyboardInput() {

		// Initialize key -> key state values
		for (Key key : Key.values()) {
			_log.debug("Initialized Key: %s, Value: %d", key.name(), key.getValue());
			_keyStates.put(key.getValue(), new KeyState());
		}
	}

	/**
	 * Checks if the passed in key was pressed this frame
	 * 
	 * @param key
	 *            the key to check
	 * @return true or false if key was pressed
	 */
	public boolean keyPressed(Key key) {
		return _keyStates.get(key.getValue()).getKeyState() == KeyState.STATE_PRESSED;
	}

	/**
	 * Checks if the passed in key is either pressed or being held
	 * 
	 * @param key
	 *            the key to check
	 * @return true or false if key is down
	 */
	public boolean keyDown(Key key) {
		return keyPressed(key) || _keyStates.get(key.getValue()).getKeyState() == KeyState.STATE_HOLDING;
	}

	/**
	 * Checks if the passed in key was released this frame
	 * 
	 * @param key
	 *            the key to check
	 * @return true or false if key was released
	 */
	public boolean keyReleased(Key key) {
		return _keyStates.get(key.getValue()).getKeyState() == KeyState.STATE_RELEASED;
	}

	/**
	 * Checks if the passed in key and any modifier keys are down
	 * 
	 * @param key
	 *            the key to check
	 * @param modKeys
	 *            they modifier keys to check
	 * @return true or false if both key and mod keys are down
	 */
	public boolean keyDownWithModifiers(Key key, Key.ModKey... modKeys) {
		int mods = Key.ModKey.NONE.getValue();

		for (Key.ModKey modKey : modKeys)
			mods |= modKey.getValue();

		return keyDown(key) && _keyStates.get(key.getValue()).getMods() == mods;
	}

	/**
	 * Reset all key states
	 */
	public void resetKeyStates() {
		for (KeyState keyState : _keyStates.values())
			keyState.reset();
	}

	/**
	 * Sets a key to be a certain state, called from input handler
	 * 
	 * @param keyValue
	 * @param keyState
	 * @param mods
	 */
	protected void keyStateChanged(int keyValue, int keyState, int mods) {
		KeyState state = _keyStates.get(keyValue);
		if (state == null) {
			_log.warn("Trying to set state for unsupported key: %d", keyValue);
			return;
		}

		// Update key state
		state.setKeyState(keyState);
		state.setMods(mods);
	}

	/**
	 * A KeyState contains the state (i.e., pressed, holding, released, none)
	 * along with any modifier keys being held down
	 * 
	 * @author brandon.porter
	 *
	 */
	private class KeyState {
		public static final int STATE_PRESSED = GLFW_PRESS;
		public static final int STATE_RELEASED = GLFW_RELEASE;
		public static final int STATE_HOLDING = GLFW_REPEAT;
		public static final int STATE_NONE = -1;

		private int _keyState;
		private int _mods;

		public KeyState() {
			this(STATE_NONE, Key.ModKey.NONE.getValue());
		}

		/**
		 * Constructs a new key state
		 * 
		 * @param keyState
		 * @param mods
		 */
		public KeyState(int keyState, int mods) {
			this._keyState = keyState;
			this._mods = mods;
		}

		/**
		 * @return current key state
		 */
		public int getKeyState() {
			return _keyState;
		}

		/**
		 * Sets the key state
		 * 
		 * @param keyState
		 */
		public void setKeyState(int keyState) {
			this._keyState = keyState;
		}

		/**
		 * @return pressed modifier keys
		 */
		public int getMods() {
			return _mods;
		}

		/**
		 * Sets the key modifiers pressed
		 * 
		 * @param mods
		 */
		public void setMods(int mods) {
			this._mods = mods;
		}

		/**
		 * Resets the current keystate to default values
		 */
		public void reset() {
			this._keyState = STATE_NONE;
			this._mods = Key.ModKey.NONE.getValue();
		}
		
		@Override
		public String toString() {
			return String.format("KeyState: %s, Mods: %s", _keyState, _mods);
		}
	}
}
