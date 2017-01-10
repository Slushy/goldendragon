package engine.graphics.geometry;

/**
 * Interface to define naming conventions for binding elements for use
 * 
 * @author Brandon
 *
 */
interface IBindable {
	/**
	 * Activates the instance for use
	 */
	void use();

	/**
	 * Deactivates the instance for use
	 */
	void done();

	/**
	 * Cleans up the instance
	 */
	void dispose();
}
