package engine;

/**
 * A simple state enum to dictate what state our engine is in
 * 
 * @author Brandon Porter
 *
 */
enum GameState {
	NOT_STARTED, INITIALIZING, LOADING_SPLASH, LOADING_SPLASH_COMPLETE, LOADING_RESOURCES, LOADING_RESOURCES_COMPLETE, LOADING_FIRST_SCENE, FIRST_SCENE_READY, RUNNING, FAILURE;

	private Exception ex;

	/**
	 * Sets an exception on this state
	 * 
	 * @param ex
	 */
	public void setException(Exception ex) {
		this.ex = ex;
	}

	/**
	 * @return the exception associated with this state
	 */
	public Exception Exception() {
		return ex;
	}
}
