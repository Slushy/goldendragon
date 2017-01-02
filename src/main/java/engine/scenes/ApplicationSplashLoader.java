package engine.scenes;

/**
 * A loader for an application splash screen
 * 
 * @author Brandon Porter
 *
 */
public abstract class ApplicationSplashLoader extends SceneLoader {

	/**
	 * Constructs an application splash loader for a splash scene
	 * 
	 * @param sceneName
	 *            name of the splash scene
	 */
	public ApplicationSplashLoader(String sceneName) {
		super(sceneName);
	}
	
	
	public abstract void loadResourcesForSceneSync();
}
