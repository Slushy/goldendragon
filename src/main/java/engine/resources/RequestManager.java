package engine.resources;

import engine.Engine;
import engine.utils.Debug;

/**
 * Sometimes we need to load resources asynchronously, or make OpenGL requests
 * while on a separate thread (OpenGL requires main thread). This request
 * manager manages the active requests and when a cycle is free it will execute
 * them in the order they've come in
 * 
 * @author Brandon Porter
 *
 */
public final class RequestManager {
	/**
	 * The graphics thread is the same as the main thread
	 */
	public static final String GRAPHICS_THREAD_NAME = Engine.MAIN_THREAD;
	/**
	 * The name of the thread running resource requests
	 */
	public static final String RESOURCE_THREAD_NAME = "Resource Thread";

	/*
	 * Processes any resource and/or other related requests in a separate thread
	 */
	private static final RequestProcessor REQUEST_PROCESSOR = new RequestProcessor(RESOURCE_THREAD_NAME);
	/*
	 * Processes only OpenGL-related requests on the main thread
	 */
	private static final GLRequestProcessor GL_REQUEST_PROCESSOR = new GLRequestProcessor();

	// Static class
	private RequestManager() {
	}

	/**
	 * Adds a non-GL request to the resource request queue and executes it in a
	 * separate thread when the request is first on the list
	 * 
	 * @param request
	 *            the non-GL request to execute in a separate thread
	 */
	public static void makeResourceRequest(IRequest request) {
		REQUEST_PROCESSOR.addRequestToQueue(request);
	}

	/**
	 * Adds an OpenGL-Only request to the OpenGL request queue and executes it
	 * on the MAIN thread as soon as there are resources available and it is top
	 * of the queue
	 * 
	 * @param request
	 *            the GL request to execute on the main thread
	 */
	public static void makeGLRequest(IRequest request) {
		GL_REQUEST_PROCESSOR.addRequestToQueue(request);
	}

	/**
	 * Attempts to execute the GL request immediately without adding it to the
	 * queue ONLY IF we are on the main thread, otherwise we just append it to
	 * the normal GL request queue.
	 * 
	 * @param request
	 *            the GL request to execute on the main thread
	 * @return true if the request was immediate, false if it was added to the
	 *         queue
	 */
	public static boolean makeGLRequestImmediate(IRequest request) {
		if (Thread.currentThread().getName().equals(GRAPHICS_THREAD_NAME)) {
			request.doRequest();
			return true;
		}

		// We are not on the graphics thread, so add it to the queue
		makeGLRequest(request);
		return false;
	}

	/**
	 * Executes the next chunk of GL requests in the order they've come in. This
	 * is the preferred call over "executeAllGLRequests" as this will execute
	 * requests on a timer and will pause if it takes too long. This method will
	 * be called at least once every frame.
	 */
	public static void executeSomeGLRequests() {
		GL_REQUEST_PROCESSOR.run(false);
	}

	/**
	 * Completes the remaining GL requests in the order they have appeared
	 * 
	 * [WARNING] - Be careful using this as if there are a substantial amount of
	 * remaining GL requests this could take a while.
	 */
	public static void executeAllGLRequests() {
		GL_REQUEST_PROCESSOR.run(true);
	}

	/**
	 * Waits for all requests (resource and graphics) to complete on a separate
	 * thread. Once complete it executes the passed in callback. [WARNING] - Use
	 * this with caution, it will wait for an unknowing amount of time possibly
	 * blocking the thread forever
	 * 
	 * @param request
	 *            callback to execute once complete
	 */
	public static void waitForAllRequestsOnSeparateThread(IRequest request) {
		// TODO: Creating new objects is bad, have a permanent thread or thread
		// pool or refactor this out.
		new Thread(() -> {

			// We loop through and see if we have any outstanding requests, if
			// we do then we wait
			while (REQUEST_PROCESSOR.hasOutstandingRequests() || GL_REQUEST_PROCESSOR.hasOutstandingRequests()) {
				Debug.log("We have outstanding requests, waiting 10 milliseconds");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Debug.error("Failed to sleep on thread");
					e.printStackTrace();
				}
			}

			// Execute the request
			Debug.log("Done waiting for outstanding requests, executing callback");
			request.doRequest();
		}).run();
	}

	/**
	 * Disposes the request processors and finishes up any necessary requests
	 */
	public static void dispose() {
		// Dispose request processor first so the separate thread can finish
		// any resource requests while the main thread finishes GL requests
		REQUEST_PROCESSOR.dispose();
		GL_REQUEST_PROCESSOR.dispose();
	}
}
