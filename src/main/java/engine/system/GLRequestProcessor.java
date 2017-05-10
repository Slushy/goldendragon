package engine.system;

import engine.TimeManager;

/**
 * Manages and processes GL-only related requests on the main thread
 * 
 * @author Brandon Porter
 *
 */
final class GLRequestProcessor implements IRequestProcessor {
	private static final double MAX_TIME_MS = 8.0;

	private final RequestQueue _requestQueue = new RequestQueue();
	private final TimeManager.Timer _timer;

	/*
	 * Constructs the GL request processor and creates a new timer
	 */
	protected GLRequestProcessor() {
		this._timer = TimeManager.CreateTimer(MAX_TIME_MS);
	}
	
	/**
	 * @return whether or not we have graphic requests to complete
	 */
	public boolean hasOutstandingRequests() {
		return !_requestQueue.isEmpty();
	}

	/**
	 * Enqueues a new GL request at the end of the queue which will be executed
	 * when the main thread has resources available
	 */
	@Override
	public void addRequestToQueue(IRequest request) {
		_requestQueue.enqueue(request);
	}

	/**
	 * Because this is run on the main thread we have to limit the number of
	 * requests we make unless we specify to explicitly do all of them
	 * 
	 * @param executeAll
	 *            If true we execute all remaining requests regardless of the
	 *            time it takes, otherwise we execute until all requests are
	 *            completed OR the max time has been reached.
	 */
	public void run(boolean executeAll) {
		if (!executeAll)
			_timer.start();

		while (!_requestQueue.isEmpty()) {
			// Removes and executes the top request
			_requestQueue.dequeue().doRequest();

			// Stop making requests if we've reached
			// our max limit
			if (!executeAll && _timer.isDone())
				break;
		}

		// Resets the timer
		_timer.reset();
	}

	/**
	 * Completes all waiting GL requests when disposed
	 */
	@Override
	public void dispose() {
		this.run(true);
	}
}
