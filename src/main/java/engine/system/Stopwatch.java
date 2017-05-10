package engine.system;

import engine.utils.TimeUtils;

public class Stopwatch {
	private double _lastBenchmark = -1;
	private double _startTime = -1;

	public Stopwatch() {
	}

	public void start() {
		this._startTime = TimeUtils.getSystemTimeSeconds();
		this._lastBenchmark = this._startTime;
	}
	
	public void restart() {
		start();
	}
	
	public void stop() {
		_lastBenchmark = -1;
		_startTime = -1;
	}
	
	public boolean isRunning() {
		return _startTime > -1;
	}

	public double getTimeSinceLastBenchmark() {
		return TimeUtils.getSystemTimeSeconds() - _lastBenchmark;
	}
	
	public double getTimeSinceStart() {
		return TimeUtils.getSystemTimeSeconds() - _startTime;
	}
	
	public double getLastBenchmark() {
		return _lastBenchmark;
	}

	/**
	 * Marks a new benchmark time and returns the time since the last benchmark
	 * in seconds
	 * 
	 * @return time since last benchmark in seconds
	 */
	public double benchmark() {
		double now = TimeUtils.getSystemTimeSeconds();
		double elapsedTime = now - _lastBenchmark;
		this._lastBenchmark = now;
		return elapsedTime;
	}
}
