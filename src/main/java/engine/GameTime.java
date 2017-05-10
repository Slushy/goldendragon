package engine;

import engine.system.Stopwatch;

/**
 * Keeps track of game time
 * 
 * @author brandon.porter
 *
 */
public class GameTime {
	public static float SPEED = 1;
	
	private static float _lastFrameTime;
	private static Stopwatch _stopwatch = new Stopwatch();

	/**
	 * Initializes the game timer
	 */
	protected static void start() {
		_stopwatch.start();
		_lastFrameTime = 0;
	}

	/**
	 * Gets the total game time elapsed since we started
	 * 
	 * @return total elapsed game time in seconds
	 */
	public static double getTimeSinceGameStart() {
		return _stopwatch.getTimeSinceStart();
	}

	/**
	 * @return the delta time of the last frame
	 */
	public static float getDeltaTime() {
		return _lastFrameTime * SPEED;
	}

	/**
	 * Gets the time since last loop time and updates the last loop time
	 * 
	 * @return time since we last checked
	 */
	protected static float benchmark() {
		_lastFrameTime = (float) _stopwatch.benchmark();
		return _lastFrameTime;
	}
}
