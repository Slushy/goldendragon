package engine.input;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Enum for every key this engine supports
 * 
 * @author brandon.porter
 *
 */
public enum Key {
	// Unknown
	UNKNOWN(GLFW_KEY_UNKNOWN), 
	// Common
	SPACE(GLFW_KEY_SPACE), APOSTRAPHE(GLFW_KEY_APOSTROPHE), COMMA(GLFW_KEY_COMMA), MINUS(GLFW_KEY_MINUS), PERIOD(GLFW_KEY_PERIOD), 
	SLASH(GLFW_KEY_SLASH), SEMICOLON(GLFW_KEY_SEMICOLON), EQUAL(GLFW_KEY_EQUAL), LEFT_BRACKET(GLFW_KEY_LEFT_BRACKET), BACKSLASH(GLFW_KEY_BACKSLASH),
	RIGHT_BRACKET(GLFW_KEY_RIGHT_BRACKET), ESCAPE(GLFW_KEY_ESCAPE), ENTER(GLFW_KEY_ENTER), TAB(GLFW_KEY_TAB), BACKSPACE(GLFW_KEY_BACKSPACE),
	INSERT(GLFW_KEY_INSERT), DELETE(GLFW_KEY_DELETE), NUM_LOCK(GLFW_KEY_NUM_LOCK),
	// Arrow Keys
	RIGHT(GLFW_KEY_RIGHT), LEFT(GLFW_KEY_LEFT), DOWN(GLFW_KEY_DOWN), UP(GLFW_KEY_UP),
	// Letters
	A(GLFW_KEY_A), B(GLFW_KEY_B), C(GLFW_KEY_C), D(GLFW_KEY_D), E(GLFW_KEY_E), F(GLFW_KEY_F), G(GLFW_KEY_G), H(GLFW_KEY_H), 
	I(GLFW_KEY_I), J(GLFW_KEY_J), K(GLFW_KEY_K), L(GLFW_KEY_L), M(GLFW_KEY_M), N(GLFW_KEY_N), O(GLFW_KEY_O), P(GLFW_KEY_P), 
	Q(GLFW_KEY_Q), R(GLFW_KEY_R), S(GLFW_KEY_S), T(GLFW_KEY_T), U(GLFW_KEY_U), V(GLFW_KEY_V), W(GLFW_KEY_W), X(GLFW_KEY_X), 
	Y(GLFW_KEY_Y), Z(GLFW_KEY_Z),
	// Numbers
	NUM_0(GLFW_KEY_0), NUM_1(GLFW_KEY_1), NUM_2(GLFW_KEY_2), NUM_3(GLFW_KEY_3), NUM_4(GLFW_KEY_4), NUM_5(GLFW_KEY_5), NUM_6(GLFW_KEY_6), NUM_7(GLFW_KEY_7),
	NUM_8(GLFW_KEY_8), NUM_9(GLFW_KEY_9),
	// Function keys
	F1(GLFW_KEY_F1), F2(GLFW_KEY_F2), F3(GLFW_KEY_F3), F4(GLFW_KEY_F4), F5(GLFW_KEY_F5), F6(GLFW_KEY_F6), F7(GLFW_KEY_F7), F8(GLFW_KEY_F8), F9(GLFW_KEY_F9),
	F10(GLFW_KEY_F10), F11(GLFW_KEY_F11), F12(GLFW_KEY_F12), F13(GLFW_KEY_F13), F14(GLFW_KEY_F14), F15(GLFW_KEY_F15), F16(GLFW_KEY_F16), F17(GLFW_KEY_F17), F18(GLFW_KEY_F18),
	F19(GLFW_KEY_F19), F20(GLFW_KEY_F20), F21(GLFW_KEY_F21), F22(GLFW_KEY_F22), F23(GLFW_KEY_F23), F24(GLFW_KEY_F24), F25(GLFW_KEY_F25),
	// Control keys
	LEFT_SHIFT(GLFW_KEY_LEFT_SHIFT), LEFT_CONTROL(GLFW_KEY_LEFT_CONTROL), LEFT_ALT(GLFW_KEY_LEFT_ALT), LEFT_SUPER(GLFW_KEY_LEFT_SUPER), RIGHT_SHIFT(GLFW_KEY_RIGHT_SHIFT),
	RIGHT_CONTROL(GLFW_KEY_RIGHT_CONTROL), RIGHT_ALT(GLFW_KEY_RIGHT_ALT), RIGHT_SUPER(GLFW_KEY_RIGHT_SUPER),
	// Keypad Numbers
	KP_0(GLFW_KEY_KP_0), KP_1(GLFW_KEY_KP_1), KP_2(GLFW_KEY_KP_2), KP_3(GLFW_KEY_KP_3), KP_4(GLFW_KEY_KP_4), KP_5(GLFW_KEY_KP_5), KP_6(GLFW_KEY_KP_6), KP_7(GLFW_KEY_KP_7),
	KP_8(GLFW_KEY_KP_8), KP_9(GLFW_KEY_KP_9),
	// Keypad Math
	KP_DECIMAL(GLFW_KEY_KP_DECIMAL), KP_DIVIDE(GLFW_KEY_KP_DIVIDE), KP_MULTIPLY(GLFW_KEY_KP_MULTIPLY), KP_SUBTRACT(GLFW_KEY_KP_SUBTRACT), KP_ADD(GLFW_KEY_KP_ADD),
	KP_ENTER(GLFW_KEY_KP_ENTER), KP_EQUAL(GLFW_KEY_KP_EQUAL),
	// Other
	GRAVE_ACCENT(GLFW_KEY_GRAVE_ACCENT), PAGE_UP(GLFW_KEY_PAGE_UP), PAGE_DOWN(GLFW_KEY_PAGE_DOWN), HOME(GLFW_KEY_HOME), END(GLFW_KEY_END),
	CAPS_LOCK(GLFW_KEY_CAPS_LOCK), SCROLL_LOCK(GLFW_KEY_SCROLL_LOCK), PRINT_SCREEN(GLFW_KEY_PRINT_SCREEN), PAUSE(GLFW_KEY_PAUSE), MENU(GLFW_KEY_MENU);
	
	/**
	 * Number of keys we have
	 */
	private static final int SIZE = Key.values().length;
	private final int _value;
	
	/*
	 * Constructs a key
	 */
	private Key(int value) {
		this._value = value;
	}
	
	/**
	 * Gets the value this key represents
	 * 
	 * @return unique value for glfw keyboard
	 */
	protected int getValue() {
		return _value;
	}
	
	/**
	 * Returns the number of keys we have
	 * 
	 * @return number of enum keys
	 */
	public static int size() {
		return SIZE;
	}
	
	/**
	 * Modifier keys to be used with other keys
	 * @author brandon.porter
	 *
	 */
	public static enum ModKey {
		NONE(0),
		SHIFT(GLFW_MOD_SHIFT),
		CONTROL(GLFW_MOD_CONTROL),
		ALT(GLFW_MOD_ALT),
		SUPER(GLFW_MOD_SUPER);
		
		/**
		 * Number of modifiers we have
		 */
		private static final int SIZE = ModKey.values().length;
		
		private final int _value;
		
		/*
		 * Construct a key modifier
		 */
		private ModKey(int value) {
			this._value = value;
		}
		
		/**
		 * Returns the number of key modifiers we have
		 * 
		 * @return number of enum key modifiers
		 */
		public static int size() {
			return SIZE;
		}
		
		/**
		 * Gets the value for the modifier
		 * 
		 * @return modifier value
		 */
		protected final int getValue() {
			return _value;
		}
	}
}
