package game.scenes;

import engine.TimeManager;
import engine.common.Behavior;
import engine.common.GameObject;
import game.scenes.loaders.TestSceneLoader;

/**
 * A behavior that controls the Test Scene script
 * 
 * @author Brandon Porter
 *
 */
public class SplashSceneBehavior extends Behavior {
	private static final float CUBE_SPEED = 175f;

	private GameObject _cube;

	/**
	 * Constructs a new behavior for the Test Scene
	 */
	public SplashSceneBehavior() {
		super(TestSceneLoader.NAME);
	}

	@SuppressWarnings("unused")
	private void start() {
		this._cube = this.getScene().findGameObject("Cube");
	}

	@SuppressWarnings("unused")
	private void update() {
		_cube.getTransform().rotate(0, CUBE_SPEED * TimeManager.getDeltaTime(), 0);
	}
}
