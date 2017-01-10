package engine;

import engine.utils.TimeUtils;

/**
 * Keeps track of game time
 * 
 * @author brandon.porter
 *
 */
public class TimeManager {
	public static float SPEED = 1;

	private static double _lastLoopTime;
	private static double _gameTimeStart;
	private static double _lastFPSCheck;
	private static int _fps;
	private static float _elapsedTime;

	/**
	 * Initializes the game timer
	 */
	protected static void start() {
		_gameTimeStart = getTime();
		_lastLoopTime = _gameTimeStart;
		_elapsedTime = 0;
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
	 * @return the delta time of the last frame
	 */
	public static float getDeltaTime() {
		return _elapsedTime * SPEED;
	}

	/**
	 * @return last loop time
	 */
	public static double getLastLoopTime() {
		return _lastLoopTime;
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
	 * Gets the time since last loop time and updates the last loop time
	 * 
	 * @return time since we last checked
	 */
	protected static float getBenchmark() {
		double time = getTime();
		_elapsedTime = (float) (time - _lastLoopTime);
		_lastLoopTime = time;
		_fps++;
		return _elapsedTime;
	}

	/**
	 * Creates a simple timer
	 * 
	 * @param timerLengthMS
	 *            The countdown length of the timer in millis
	 * @return new timer
	 */
	public static Timer CreateTimer(double timerLengthMS) {
		return new Timer(timerLengthMS);
	}

	/**
	 * Creates a simple timer
	 * 
	 * @return new timer
	 */
	public static Timer CreateTimer() {
		return new Timer();
	}

	/**
	 * Timer for managing tasks when time is of the essence
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static class Timer {
		private double _timerLengthNS = -1;
		private long _startTimeNS = 0;
		private boolean _started = false;

		/**
		 * Constructs a new timer
		 */
		private Timer() {
		}

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
		 * Resets the timer with the specified length and starts it
		 * 
		 * @param timerLengthMS
		 *            the countdown length of the timer in milliseconds
		 */
		public void start(double timerLengthMS) {
			reset(timerLengthMS);
			start();
		}

		/**
		 * Starts the timer with the default time
		 */
		public void start() {
			this._startTimeNS = TimeManager.getTimeNS();
			this._started = true;
		}

		/**
		 * @return true if the timer has been started
		 */
		public boolean hasStarted() {
			return _started;
		}

		/**
		 * @return the time remaining in nanoseconds
		 */
		public double getTimeRemaining() {
			// Return the normal timer length if haven't started yet
			if (!hasStarted())
				return _timerLengthNS;

			// Returns 0 if timer is done
			return isDone() ? 0.0 : _timerLengthNS - getTimeTaken();
		}

		/**
		 * @return the time taken in nanoseconds
		 */
		public double getTimeTaken() {
			// Returns 0 if timer hasn't started
			return !hasStarted() ? 0.0 : TimeManager.getTimeNS() - _startTimeNS;
		}

		/**
		 * Checks if the current timer is done. If it is done it resets the
		 * timer
		 * 
		 * @return true if the timer hasnt been started, or the time taken >=
		 *         timer length passed in
		 */
		public boolean isDone() {
			boolean isDone = hasStarted() && getTimeTaken() >= _timerLengthNS;
			// Reset the timer if we just finished
			if (isDone)
				reset();
			return isDone;
		}
	}
}
