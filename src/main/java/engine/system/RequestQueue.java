package engine.system;

import java.util.LinkedList;

/**
 * Manages a thread-safe queue of requests for processing by the request manager
 * 
 * @author Brandon Porter
 *
 */
public class RequestQueue {
	private LinkedList<IRequest> _requestQueue = new LinkedList<>();

	// Prevent instantiation outside of package
	protected RequestQueue() {
	}

	/**
	 * Adds a new request to the end of the request queue
	 * 
	 * @param request
	 *            the request to add
	 */
	public synchronized void enqueue(IRequest request) {
		_requestQueue.addLast(request);
	}

	/**
	 * @return Removes the top request from the queue
	 */
	public synchronized IRequest dequeue() {
		return _requestQueue.removeFirst();
	}

	/**
	 * @return true if the request queue is empty, false if we have requests
	 */
	public synchronized boolean isEmpty() {
		return _requestQueue.isEmpty();
	}
}
