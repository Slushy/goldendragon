package engine;

/**
 * The game manager is used by the engine to initialize, update, and render the
 * game
 * 
 * @author Brandon Porter
 *
 */
class GameManager {
	private final IGameInitializer _gameInitializer;

	/**
	 * Constructs a game manager
	 * 
	 * @param gameInitializer
	 */
	public GameManager(IGameInitializer gameInitializer) {
		this._gameInitializer = gameInitializer;
	}
	
	/**
	 * First begins by loading the scene manager and showing the application
	 * splash as quickly as possible
	 */
	public void init() {
		_gameInitializer.loadResources();
	}
	
	public void update() {
		// TODO: IMPLEMENT ME PLZ
	}
	
	public void render() {
		// TODO: IMPLEMENT ME PLZ
	}
	
	public void dispose() {
		// TODO: IMPLEMENT ME PLZ
	}
}
