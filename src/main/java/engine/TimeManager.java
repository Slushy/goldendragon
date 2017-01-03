package engine;

import engine.utils.TimeUtils;

/**
 * Keeps track of game time
 * 
 * @author brandon.porter
 *
 */
public class TimeManager {
	private static double _lastLoopTime;
	private static double _gameTimeStart;
	private static double _lastFPSCheck;
	private static int _fps;

	/**
	 * Initializes the game timer
	 */
	protected static void start() {
		_gameTimeStart = getTime();
		_lastLoopTime = _gameTimeStart;
		_fps = 0;
	}

	/**
	 * Gets the total game time elapsed since we started
	 * 
	 * @return total elapsed game time in seconds
	 */
	public static double getGameTime() {
		return getTime() - _gameTimeStart;
	}

	/**
	 * Gets the overall time in seconds
	 * 
	 * @return overall clock time
	 */
	public static double getTime() {
		return TimeUtils.NanoToSeconds(getTimeNS());
	}

	/**
	 * @return the overall time in nanoseconds
	 */
	public static long getTimeNS() {
		return System.nanoTime();
	}

	/**
	 * Gets the time since last loop time and updates the last loop time
	 * 
	 * @return time since we last checked
	 */
	public static float getElapsedTime() {
		double time = getTime();
		float elapsedTime = (float) (time - _lastLoopTime);
		_lastLoopTime = time;
		_fps++;
		return elapsedTime;
	}

	/**
	 * @return last loop time
	 */
	public static double getLastLoopTime() {
		return _lastLoopTime;
	}

	/**
	 * Creates a simple timer
	 * 
	 * @param timerLengthMS
	 *            The countdown length of the timer in millis
	 * @return new stop watch
	 */
	public static Timer CreateTimer(double timerLengthMS) {
		return new Timer(timerLengthMS);
	}

	/**
	 * If it has been > 1s since last checked, it will get the correct fps and
	 * reset it, otherwise it will return -1
	 * 
	 * @return Correct FPS or -1 if not yet time to check
	 */
	public static int getFPS() {
		int fps = -1;

		if (getLastLoopTime() - _lastFPSCheck > 1.0) {
			_lastFPSCheck = getLastLoopTime();
			fps = _fps;
			_fps = 0;
		}

		return fps;
	}

	/**
	 * Timer for managing tasks when time is of the essence
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Timer {
		private double _timerLengthNS;
		private long _startTimeNS;
		private boolean _started;

		/**
		 * Constructs a new timer
		 * 
		 * @param timerLengthMs
		 *            The countdown length of the timer in milliseconds
		 */
		private Timer(double timerLengthMS) {
			// We do the timer in nanoseconds
			this.reset(timerLengthMS);
		}

		/**
		 * Resets the timer with the same time length
		 */
		public void reset() {
			this._started = false;
		}

		/**
		 * Resets the timer with a new time length
		 * 
		 * @param newTimerLengthMS
		 *            the new countdown length of the timer in milliseconds
		 */
		public void reset(double newTimerLengthMS) {
			reset();
			this._timerLengthNS = TimeUtils.MilliToNano(newTimerLengthMS);
		}

		/**
		 * Starts the timer
		 */
		public void Start() {
			this._startTimeNS = TimeManager.getTimeNS();
			this._started = true;
		}

		/**
		 * @return the time remaining in nanoseconds
		 */
		public double getTimeRemaining() {
			// Return the normal timer length if haven't started yet
			if (!_started)
				return _timerLengthNS;

			// Returns 0 if timer is done
			return IsDone() ? 0.0 : _timerLengthNS - getTimeTaken();
		}

		/**
		 * @return the time taken in nanoseconds
		 */
		public double getTimeTaken() {
			// Returns 0 if timer hasn't started
			return !_started ? 0.0 : TimeManager.getTimeNS() - _startTimeNS;
		}

		/**
		 * Checks if the current timer is done
		 * 
		 * @return true if the time taken >= timer length passed in
		 */
		public boolean IsDone() {
			return getTimeTaken() >= _timerLengthNS;
		}
	}
}
