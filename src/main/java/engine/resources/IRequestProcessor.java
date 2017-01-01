package engine.resources;

/**
 * Represents the signatures for a request processor
 * 
 * @author Brandon Porter
 *
 */
public interface IRequestProcessor {
	/**
	 * Adds a new request to the request queue
	 * 
	 * @param request
	 *            the request to execute
	 */
	void addRequestToQueue(IRequest request);

	/**
	 * Disposes of the processor
	 */
	void dispose();
}
