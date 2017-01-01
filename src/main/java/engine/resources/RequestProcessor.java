package engine.resources;

import engine.utils.Debug;

/**
 * Manages and processes non-GL related requests on a separate thread
 * 
 * @author Brandon Porter
 *
 */
final class RequestProcessor extends Thread implements IRequestProcessor {
	private final RequestQueue _requestQueue = new RequestQueue();
	private boolean _active = true;

	/**
	 * Constructs a request processor thread and starts it
	 */
	protected RequestProcessor() {
		this.start();
	}

	/**
	 * Running in the processor thread, it continues picking off requests until
	 * it is finished and finally suspends until notified of more
	 */
	@Override
	public synchronized void run() {
		while (_active || !_requestQueue.isEmpty()) {
			if (!_requestQueue.isEmpty()) {
				// Removes and executes the top request
				_requestQueue.dequeue().doRequest();
			} else {
				// Suspend thread until notified of new requests
				try {
					this.wait();
				} catch (InterruptedException e) {
					Debug.error("RequestProcessor thread was interrupted.");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Adds a new request to the request queue
	 * 
	 * @param request
	 *            the request to execute
	 */
	@Override
	public void addRequestToQueue(IRequest request) {
		boolean isPaused = _requestQueue.isEmpty();
		_requestQueue.enqueue(request);
		// Todo: better way to determine if paused
		if (isPaused) {
			// Notify the current thread to continue if currently paused
			indicateNewRequests();
		}
	}

	/**
	 * Disposes the processor thread by setting it to inactive and notifying the
	 * thread if currently in a waiting state to finish off any requests
	 */
	@Override
	public void dispose() {
		this._active = false;
		indicateNewRequests();
	}

	/*
	 * Wakes up the processor thread from a "waiting" state to inform of new
	 * requests
	 */
	private synchronized void indicateNewRequests() {
		notify();
	}
}
