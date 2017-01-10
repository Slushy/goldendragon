package engine.scenes;

/**
 * A loader for an application splash screen
 * 
 * @author Brandon Porter
 *
 */
public abstract class ApplicationSplashLoader extends SceneLoader {
	private final double _minTimeToShow;

	/**
	 * Constructs an application splash loader for a splash scene that will be
	 * hidden as soon as the next scene has been loaded
	 * 
	 * @param sceneName
	 *            name of the splash scene
	 */
	public ApplicationSplashLoader(String sceneName) {
		this(sceneName, -1);
	}

	/**
	 * Constructs a splash loader for a splash scene that will remain visible
	 * for AT LEAST the specified minimum time in the case the next scene has
	 * been loaded very quickly
	 * 
	 * @param sceneName
	 *            name of the splash scene
	 * @param minTimeToShow
	 *            minimum amount of time (in milliseconds) the splash should be
	 *            visible before showing the first scene in the case where the
	 *            game and first scene loads very quickly
	 */
	public ApplicationSplashLoader(String sceneName, double minTimeToShow) {
		super(sceneName);
		this._minTimeToShow = minTimeToShow;
	}

	/**
	 * Get the minimum amount of time (in milliseconds) the splash should be
	 * visible before showing the first scene in the case where the game and
	 * first scene loads very quickly
	 * 
	 * @return Anything > 0 for a minimum time, otherwise splash will be removed
	 *         as soon as first scene loads
	 */
	public final double getMinimumTimeSplashIsDisplayed() {
		return _minTimeToShow;
	}
}
