package engine;

/**
 * Keeps track of game time
 * @author brandon.porter
 *
 */
public class Timer {
	private double _lastLoopTime;
	private double _gameTimeStart;
	
	/**
	 * Initializes the game timer
	 */
	public void init() {
		this._gameTimeStart = getTime();
		this._lastLoopTime = _gameTimeStart;
	}
	
	/**
	 * Gets the total game time elapsed since we started
	 * @return total elapsed game time in seconds
	 */
	public double getGameTime() {
		return getTime() - _gameTimeStart;
	}
	
	/**
	 * Gets the overall time in seconds
	 * @return overall clock time
	 */
	public double getTime() {
		return System.nanoTime() / 1_000_000_000.0;
	}
	
	/**
	 * Gets the time since last loop time and updates the last loop time
	 * @return time since we last checked
	 */
	public float getElapsedTime() {
		double time = getTime();
		float elapsedTime = (float) (time - _lastLoopTime);
		_lastLoopTime = time;
		return elapsedTime;
	}
	
	/**
	 * @return last loop time
	 */
	public double getLastLoopTime() {
		return _lastLoopTime;
	}
}
