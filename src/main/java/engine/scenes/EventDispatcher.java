package engine.scenes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import engine.common.Component;
import engine.graphics.SceneRenderer;
import engine.input.InputHandler;

/**
 * The scene uses an event dispatcher to send events to any of its applicable
 * components
 * 
 * @author Brandon Porter
 *
 */
final class EventDispatcher {

	private final Map<ExecutionEvent, LinkedList<ComponentMethod>> _subscribedComponents = new HashMap<>();
	private final Map<Component, Integer> _componentCapabilities = new HashMap<>();

	/**
	 * Constructs a new event dispatcher
	 */
	public EventDispatcher() {
		// Initializes the event map with empty lists
		for (ExecutionEvent evt : ExecutionEvent.values()) {
			_subscribedComponents.put(evt, new LinkedList<ComponentMethod>());
		}
	}

	/**
	 * Subscribes the component for each of the specified events
	 * 
	 * @param component
	 *            the component to subscribe
	 * @param events
	 *            the events on which to subscribe the component to
	 */
	public void subscribeComponent(Component component, Map<EventDispatcher.ExecutionEvent, Method> events) {
		int subscribedEvents = 0;
		for (ExecutionEvent evt : events.keySet()) {
			_subscribedComponents.get(evt).add(new ComponentMethod(component, events.get(evt)));
			subscribedEvents |= evt.bitValue();
		}

		// Keep track of the events per component for faster removal
		_componentCapabilities.put(component, subscribedEvents);
	}

	/**
	 * Removes the component from all subscribed events
	 * 
	 * @param component
	 *            the component to remove completely
	 */
	public void removeComponent(Component component) {
		int subscribedEvents = _componentCapabilities.get(component);
		if (subscribedEvents <= 0)
			return;

		for (ExecutionEvent evt : ExecutionEvent.values()) {
			// Only check the list if we know it is subscribed
			if ((subscribedEvents & evt.bitValue()) == evt.bitValue()) {
				_subscribedComponents.get(evt).remove(component);
			}
		}
		// Remove from component capabilities
		_componentCapabilities.remove(component);
	}

	/**
	 * Dispatches the event to every subscribed component
	 * 
	 * @param event
	 *            the event to call on every component
	 * @param args
	 *            the arguments to pass along
	 */
	public void dispatchEvent(ExecutionEvent event, Object... args) {
		for (ComponentMethod compMethod : _subscribedComponents.get(event)) {
			Component comp = compMethod.component;
			Method meth = compMethod.method;
			try {
				//meth.invoke(comp, args);
				if (event == ExecutionEvent.INITIALIZE) {
					comp.init();
				} else if (event == ExecutionEvent.RENDER) {
					comp.render((SceneRenderer) args[0]);
				} else if (event == ExecutionEvent.UPDATE) {
					comp.update((InputHandler) args[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Clears the subscribed components
	 */
	public void dispose() {
		_subscribedComponents.clear();
	}
	
	private static class ComponentMethod {
		public final Component component;
		public final Method method;
		
		public ComponentMethod(Component comp, Method meth) {
			this.component = comp;
			this.method = meth;
		}
	}
	/**
	 * The possible types of events a component can listen to
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static enum ExecutionEvent {
		INITIALIZE(1, "init"), ON_FOREGROUND(2, "onForeground"), UPDATE(4, "update", InputHandler.class), RENDER(8,
				"render", SceneRenderer.class), ON_BACKGROUND(16, "onBackground");

		private final int _bitValue;
		private final String _methodName;
		private final Class<?>[] _parameterTypes;

		private ExecutionEvent(int bitValue, String methodName) {
			this(bitValue, methodName, new Class<?>[0]);
		}

		private ExecutionEvent(int bitValue, String methodName, Class<?>... paramTypes) {
			this._bitValue = bitValue;
			this._methodName = methodName;
			this._parameterTypes = paramTypes;
		}

		/**
		 * @return the bit value of the event
		 */
		public int bitValue() {
			return _bitValue;
		}

		/**
		 * @return the events method name that should be called on the component
		 */
		public String methodName() {
			return _methodName;
		}

		/**
		 * @return the parameter types of the event method
		 */
		public Class<?>[] methodParams() {
			return _parameterTypes;
		}

		/**
		 * Loops over and compares the events params with the passed in method
		 * params
		 * 
		 * @param parameters
		 *            the parameters matching the method args
		 * @return true if the params match, false otherwise
		 */
		public boolean paramsMatch(Class<?>[] parameters) {
			if (parameters.length != this._parameterTypes.length)
				return false;

			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i] != _parameterTypes[i])
					return false;
			}
			return true;
		}
	}
}
