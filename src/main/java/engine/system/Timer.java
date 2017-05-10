package engine.system;

import engine.utils.Debug;
import engine.utils.TimeUtils;

/**
 * Timer class which counts down from a given time.
 * 
 * @author Brandon Porter
 *
 */
public class Timer {
	private double _timerLengthNS = -1;
	private long _startTimeNS = 0;
	private boolean _started = false;

	/**
	 * Constructs a new timer with no time length. There must be a length
	 * before the timer can start.
	 */
	public Timer() {
		this(-1);
	}
	
	/**
	 * Constructs a new timer
	 * 
	 * @param timerLengthMs
	 *            The countdown length of the timer in milliseconds
	 */
	public Timer(double timerLengthMS) {
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
	 * Starts the timer with the current timer length
	 */
	public void start() {
		if (_timerLengthNS < 0)
			Debug.error("No timer length has been set.");
		
		this._startTimeNS = TimeUtils.getSystemTimeNS();
		this._started = true;
	}

	/**
	 * @return true if the timer has been started
	 */
	public boolean hasStarted() {
		return _started;
	}

	/**
	 * @return the time remaining in milliseconds
	 */
	public double getTimeRemaining() {
		// Return the normal timer length if haven't started yet
		if (!hasStarted())
			return _timerLengthNS;

		// Returns 0 if timer is done
		double nsRemaining =  isDone() ? 0.0 : _timerLengthNS - getTimeTaken();
		return TimeUtils.NanoToMilli(nsRemaining);
	}

	/**
	 * @return the time taken since the start of the timer in milliseconds
	 */
	public double getTimeTaken() {
		return TimeUtils.NanoToMilli(getTimeTakenNS());
	}

	/**
	 * Checks if the current timer is done. If it is done it resets the
	 * timer
	 * 
	 * @return true if the timer hasnt been started, or the time taken >=
	 *         timer length passed in
	 */
	public boolean isDone() {
		boolean isDone = hasStarted() && getTimeTakenNS() >= _timerLengthNS;
		// Reset the timer if we just finished
		if (isDone)
			reset();
		return isDone;
	}
	
	/**
	 * @return the time taken since the start of the timer in nanoseconds
	 */
	private double getTimeTakenNS() {
		// Returns 0 if timer hasn't started
		return !hasStarted() ? 0.0 : TimeUtils.getSystemTimeNS() - _startTimeNS;
	}
}
