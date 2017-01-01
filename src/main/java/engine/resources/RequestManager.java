package engine.resources;

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
	/*
	 * Processes any resource and/or other related requests in a separate thread
	 */
	private static final RequestProcessor REQUEST_PROCESSOR = new RequestProcessor();
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
	 * Executes the next chunk of GL requests in the order they've come in. This
	 * is the preferred call over "executeAllGLRequests" as this will execute
	 * requests on a timer and will pause if it takes too long. This method will be
	 * called at least once every frame.
	 */
	public static void executeSomeGLRequests() {
		GL_REQUEST_PROCESSOR.run(false);
	}

	/**
	 * Completes the remaining GL requests in the order they have appeared
	 * 
	 * [WARNING] - Be careful using this as if there are a substantial amount of
	 *             remaining GL requests this could take a while.
	 */
	public static void executeAllGLRequests() {
		GL_REQUEST_PROCESSOR.run(true);
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
