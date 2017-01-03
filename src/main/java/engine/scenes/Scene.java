package engine.scenes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	private final List<GameObject> _gameObjects = new LinkedList<>();

	private boolean _isReady = false;

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
		for (GameObject obj : _gameObjects) {
			if (obj.getName().equalsIgnoreCase(name))
				return obj;
		}

		return null;
	}
	
	/**
	 * Finds all game object in the scene by name
	 * 
	 * @param name
	 *            of the game object to locate
	 * @return the found game object or null
	 */
	public List<GameObject> findGameObjects(String name) {
		List<GameObject> gameObjects = new LinkedList<>();
		for (GameObject obj : _gameObjects) {
			if (obj.getName().equalsIgnoreCase(name))
				gameObjects.add(obj);
		}

		return gameObjects;
	}

	/**
	 * Adds the game object to the scene
	 * 
	 * @param gameObject
	 */
	public void addGameObject(GameObject gameObject) {
		// Adds object to scene
		_gameObjects.add(gameObject);
		gameObject.addedToScene(this);

		// TODO: Fix to only check classes we have not previously checked before
		Map<EventDispatcher.ExecutionEvent, Method> compEvents = new HashMap<>();
		for (Component comp : gameObject.getComponents()) {
			for (Method m : comp.getClass().getDeclaredMethods()) {

				// Loop over every execution method a component can have
				for (EventDispatcher.ExecutionEvent evt : EventDispatcher.ExecutionEvent.values()) {
					if (m.getName().equals(evt.methodName())) {
						m.setAccessible(true);
						compEvents.put(evt, m);

						// Scene has already been initialized, so we
						// initialize the game object
						if (_isReady && evt == ExecutionEvent.INITIALIZE) {
							_eventDispatcher.invokeMethod(comp, m);
						}
					}

				}
			}

			// Subscribe the component to the events
			_eventDispatcher.subscribeComponent(comp, compEvents);
			compEvents.clear();
		}
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
		return _isReady;
	}
	
	/**
	 * Initializes the scene
	 * 
	 * @throws Exception
	 */
	protected void init() throws Exception {
		SceneRenderer.instance().reset();
		// Initializes any components that require it
		_eventDispatcher.dispatchEvent(ExecutionEvent.INITIALIZE);
		this._isReady = true;
	}

	/**
	 * When the scene becomes active this is fired
	 */
	public void onForeground() {
		_eventDispatcher.dispatchEvent(ExecutionEvent.ON_FOREGROUND);
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
		this._isReady = false;
		_eventDispatcher.dispose();

		for (GameObject gameObject : _gameObjects) {
			gameObject.dispose();
		}
	}
}
