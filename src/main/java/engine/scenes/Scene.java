package engine.scenes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import engine.common.Camera;
import engine.common.Component;
import engine.common.Entity;
import engine.common.GameObject;
import engine.scenes.EventDispatcher.ExecutionEvent;

/**
 * Each "screen" of the game will be a type of scene that holds onto all of the
 * game objects within this scene and will render/update them as the engine
 * demands
 * 
 * @author Brandon Porter
 *
 */
public class Scene extends Entity {
	private final EventDispatcher _eventDispatcher = new EventDispatcher();

	private SceneState _sceneState = SceneState.INACTIVE;
	private Map<EventDispatcher.ExecutionEvent, Method> _compEvents = new HashMap<>();
	private HashMap<String, ArrayList<GameObject>> _gameObjects = new HashMap<>();
	/**
	 * Constructs a new scene with the specified name
	 * 
	 * @param name
	 *            unique name of the scene
	 */
	public Scene(String name) {
		super(name);
	}

	/**
	 * Finds the first game object in the scene with the specified name
	 * 
	 * @param name
	 *            of the game object to locate
	 * @return the found game object or null
	 */
	public GameObject findGameObject(String name) {
		ArrayList<GameObject> gameObjects = _gameObjects.get(name);
		if (gameObjects == null)
			return null;
		
		return gameObjects.get(0);
	}

	/**
	 * Finds all game object in the scene by name
	 * 
	 * @param name
	 *            of the game object to locate
	 * @return the found game object or null
	 */
	public List<GameObject> findGameObjects(String name) {
		ArrayList<GameObject> gameObjects = _gameObjects.get(name);
		if (gameObjects == null)
			return null;
		
		return gameObjects;
	}

	/**
	 * Adds the game object to the scene and processes each of its components
	 * 
	 * @param gameObject
	 *            the game object instance to add to the scene
	 */
	public void addGameObject(GameObject gameObject) {
		ArrayList<GameObject> gameObjects = _gameObjects.get(gameObject.getName());
		// Create the list with the name of the gameObject as key if it doesn't exist
		if (gameObjects == null) {
			gameObjects = new ArrayList<GameObject>();
			_gameObjects.put(gameObject.getName(), gameObjects);
		} 
		// If it's already been added to the scene then return
		else if (gameObjects.contains(gameObject))
			return;
		
		// Adds object to scene
		gameObjects.add(gameObject);
		gameObject.addedToScene(this, this::processComponent);

		// Loop over and process each component in the added game object
		for (Component comp : gameObject.getComponents()) {
			processComponent(comp);
		}
		
		// Add each child to the scene
		for (GameObject child : gameObject.getChildren())
			addGameObject(child);
	}

	/**
	 * @return the camera for the scene
	 */
	public Camera getCamera() {
		return Camera.MAIN;
	}

	/**
	 * @return the renderer for the scene
	 */
	public SceneRenderer getRenderer() {
		return SceneRenderer.instance();
	}

	/**
	 * @return true if the scene is ready to show, false otherwise
	 */
	protected final boolean isReady() {
		return _sceneState == SceneState.READY;
	}

	/**
	 * Initializes the scene, this may happen in the background while another
	 * scene is still running
	 * 
	 * @throws Exception
	 */
	protected void init() throws Exception {
		this._sceneState = SceneState.INITIALIZING;

		// Initializes any components that require it
		_eventDispatcher.dispatchEvent(ExecutionEvent.INITIALIZE);

		// TODO: Instead of saying its ready, maybe we should find a way to make
		// sure all game objects have been initialized and aren't loading
		// anything before saying we are "READY"
		this._sceneState = SceneState.READY;
	}

	/**
	 * When the scene first starts (becomes the active scene) this is fired
	 */
	public void start() {
		// We are now the active scene
		this._sceneState = SceneState.ACTIVE;

		// Reset the scene renderer to inform it a new scene is ready to start
		SceneRenderer.instance().reset();

		// Starts any components that require it
		_eventDispatcher.dispatchEvent(ExecutionEvent.START);
	}

	/**
	 * Updates the scene
	 */
	public void update() {
		_eventDispatcher.dispatchEvent(ExecutionEvent.UPDATE);
	}

	/**
	 * Renders the scene
	 * 
	 * @param graphics
	 *            the graphics controller for the current display
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void render() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		// Renders the necessary components
		SceneRenderer.instance().render(this);
	}

	/**
	 * Disposes the scene
	 */
	@Override
	protected void onDispose() {
		this._sceneState = SceneState.CLOSING;
		_eventDispatcher.dispose();

		for (ArrayList<GameObject> gameObjects : _gameObjects.values()) {
			for(GameObject gameObject : gameObjects)
				gameObject.dispose();
			gameObjects.clear();
		}
		_gameObjects.clear();
	}

	/**
	 * Called for each component in the scene once to set it up for the proper
	 * events
	 * 
	 * @param comp
	 *            the component that is part of the scene
	 */
	private void processComponent(Component comp) {
		_compEvents.clear();
		
		// TODO: Fix to only check classes we have not previously checked before
		for (Method m : comp.getClass().getDeclaredMethods()) {

			// Loop over every execution method a component can have
			for (EventDispatcher.ExecutionEvent evt : EventDispatcher.ExecutionEvent.values()) {
				if (m.getName().equals(evt.methodName())) {
					// Set the method as essentially "public" so we can
					// invoke it, as these methods are usually defined as
					// private or protected.
					m.setAccessible(true);

					// If the scene is in any state other than
					// INACTIVE, this means they are being added from
					// other components so we can go ahead and
					// initialize them now.
					if (evt == ExecutionEvent.INITIALIZE && _sceneState.greaterThan(SceneState.INACTIVE)) {
						_eventDispatcher.invokeMethod(comp, m);
					} else {
						// Dont add the initialize event to those game
						// objects that are being added dynamically
						// because we only initialize once
						_compEvents.put(evt, m);
					}

					// If the scene has already started then lets invoke the
					// START method on the new supported component (we still
					// want to subscribe it since it could be called later
					// if a disabled game object becomes enabled again)
					if (evt == ExecutionEvent.START && _sceneState.greaterThan(SceneState.READY)) {
						_eventDispatcher.invokeMethod(comp, m);
					}
				}
			}
		}

		// Subscribe the component to the events
		_eventDispatcher.subscribeComponent(comp, _compEvents);
	}

	/**
	 * The different states a scene can be in (this will most likely be changed)
	 * 
	 * @author Brandon Porter
	 *
	 */
	private enum SceneState {
		INACTIVE(0), INITIALIZING(1), READY(2), STARTING(3), ACTIVE(4), CLOSING(5);

		private final int _rank;

		/**
		 * Constructs a scene state of certain rank
		 * 
		 * @param rank
		 *            the relative "level" compared to other scene states
		 */
		private SceneState(int rank) {
			this._rank = rank;
		}

		/**
		 * @param state
		 *            SceneState to compare against
		 * @return true if the current scene state is greater in rank than the
		 *         passed in scene state
		 */
		public boolean greaterThan(SceneState state) {
			return getRank() > state.getRank();
		}

		/**
		 * @return the current rank of this scene state used to compare against
		 *         other scene states
		 */
		public int getRank() {
			return _rank;
		}
	}
}
