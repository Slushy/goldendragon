package engine;

import engine.utils.TimeUtils;

/**
 * Keeps track of game time
 * 
 * @author brandon.porter
 *
 */
public class TimeManager {
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
	 * 
	 * @return total elapsed game time in seconds
	 */
	public double getGameTime() {
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
