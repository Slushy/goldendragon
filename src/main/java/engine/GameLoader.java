package engine;

import java.util.function.Consumer;

import engine.resources.RequestManager;
import engine.scenes.ApplicationSplashLoader;

/**
 * Our game loader that is run right after the engine is initialized. It loads
 * and shows the app splash screen, loads the game resources, and loads the
 * first scene.
 * 
 * @author Brandon Porter
 *
 */
final class GameLoader {
	private final IGameInitializer _gameInitializer;

	/**
	 * Constructs our game loader
	 * 
	 * @param gameInitializer
	 *            the game-specific initializer passed into the engine
	 */
	public GameLoader(IGameInitializer gameInitializer) {
		this._gameInitializer = gameInitializer;
	}

	/**
	 * Loads and shows the application splash (if exists), loads any
	 * game-specific resources, and finally loads the very first scene of the
	 * game
	 * 
	 * @param onLoadingComplete
	 *            a callback function taking a string. The string value
	 *            represents the error that occurred during loading. If an error
	 *            did not occur it will be null.
	 */
	public void load(Consumer<String> onLoadingComplete) {
		// First thing is first, load splash screen (Should be synchronous since
		// we are on main thread so we can wait on it)
		ApplicationSplashLoader appSplashLoader = _gameInitializer.getApplicationSplashLoader();
		if (appSplashLoader != null) {
			try {
				SceneManager.loadAndShowApplicationSplash(appSplashLoader);
			} catch (Exception e) {
				e.printStackTrace();

				// For now simply set error and return if we fail to load splash
				// Obviously in practice we won't want to stop a game from
				// loading if the splash fails unless it is a core part of the
				// game.
				onLoadingComplete.accept("Error loading application splash");
				return;
			}
		}

		// Begin loading game resources in separate thread
		String firstScene = _gameInitializer.getSceneLoaders()[0].sceneName;
		RequestManager.makeResourceRequest(() -> {
			try {
				_gameInitializer.loadResources();

				// Wait for all additional resource and graphics requests to
				// complete before we begin loading the scene
				RequestManager.waitForAllRequestsOnSeparateThread(() -> {
					loadFirstScene(firstScene, onLoadingComplete);
				});

			} catch (Exception e) {
				e.printStackTrace();
				onLoadingComplete.accept("Error loading game resources");
			}
		});
	}

	/**
	 * Called when the game is closing; this should unload all resources created
	 */
	public void dispose() {
		_gameInitializer.dispose();
	}

	/*
	 * Loads the first scene
	 */
	private void loadFirstScene(String firstScene, Consumer<String> onLoadingComplete) {
		String errMsg = null;
		try {
			if (!SceneManager.loadScene(firstScene))
				errMsg = "Failed to load the first scene";
		} catch (Exception e) {
			e.printStackTrace();
			errMsg = "An exception occurred trying to load the first scene";
		} finally {
			onLoadingComplete.accept(errMsg);
		}
	}
}
