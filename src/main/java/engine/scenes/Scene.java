package engine.scenes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.GameDisplay;
import engine.GraphicsController;
import engine.common.Component;
import engine.common.GameObject;
import engine.common.gameObjects.Camera;
import engine.input.InputHandler;
import engine.input.Key;
import engine.input.KeyboardInput;
import engine.scenes.EventDispatcher.ExecutionEvent;

/**
 * Each "screen" of the game will be a type of scene that holds onto all of the
 * game objects within this scene and will render/update them as the engine
 * demands
 * 
 * @author Brandon Porter
 *
 */
public class Scene {
	private final EventDispatcher _eventDispatcher = new EventDispatcher();
	private final List<GameObject> _gameObjects = new ArrayList<>();
	private final SceneRenderer _sceneRenderer = new SceneRenderer();

	private final Camera _camera = new Camera();
	private final String _name;

	private boolean _isReady = false;

	private List<Component> _components = new ArrayList<>();

	/**
	 * Constructs a new scene with the specified name
	 * 
	 * @param name
	 *            unique name of the scene
	 */
	public Scene(String name) {
		this._name = name;
		// For now every scene will have a default camera
		this.addGameObject(_camera);
	}

	/**
	 * Finds a game object in the scene by name
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
			_components.add(comp);
			for (Method m : comp.getClass().getDeclaredMethods()) {

				// Loop over every execution method a component can have
				for (EventDispatcher.ExecutionEvent evt : EventDispatcher.ExecutionEvent.values()) {
					if (m.getName().equals(evt.methodName())) {
						m.setAccessible(true);
						compEvents.put(evt, m);
					}
				}
			}

			// Subscribe the component to the events
			_eventDispatcher.subscribeComponent(comp, compEvents);
			compEvents.clear();
		}
	}

	/**
	 * Initializes the scene
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		_sceneRenderer.init(this);
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
	 * When the currently active scene is no longer active, this is fired
	 */
	public void onBackground() {
		_eventDispatcher.dispatchEvent(ExecutionEvent.ON_BACKGROUND);
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
	public void render() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		_sceneRenderer.preRender();

		// Renders the necessary components
		_eventDispatcher.dispatchEvent(ExecutionEvent.RENDER);
		_sceneRenderer.render();
		_sceneRenderer.endRender();
	}

	/**
	 * @return name of this scene
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @return the camera for the scene
	 */
	public Camera getCamera() {
		return _camera;
	}

	/**
	 * @return the renderer for the scene
	 */
	public SceneRenderer getRenderer() {
		return _sceneRenderer;
	}

	/**
	 * Disposes the scene
	 */
	public void dispose() {
		this._isReady = false;
		for (GameObject gameObject : _gameObjects) {
			gameObject.dispose();
		}
	}

	/**
	 * @return true if the scene is ready to show, false otherwise
	 */
	protected final boolean isReady() {
		return _isReady;
	}
}
