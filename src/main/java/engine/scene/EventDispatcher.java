package engine.scene;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import engine.utils.Debug;

/**
 * The scene uses an event dispatcher to send events to any of its applicable
 * components
 * 
 * @author Brandon Porter
 *
 */
final class EventDispatcher {
	private final Map<ExecutionEvent, LinkedList<ComponentMethod>> _subscribedComponents = new HashMap<>();

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
		for (ExecutionEvent evt : events.keySet()) {
			_subscribedComponents.get(evt).add(new ComponentMethod(component, events.get(evt)));
		}
	}

	/**
	 * Dispatches the event to every subscribed component
	 * 
	 * @param event
	 *            the event to call on every component
	 * @param args
	 *            the arguments to pass along
	 */
	public void dispatchEvent(ExecutionEvent event) {
		LinkedList<ComponentMethod> compMethods = _subscribedComponents.get(event);
		for (ComponentMethod compMethod : compMethods) {
			Component comp = compMethod.component;
			// For now we are just going to continue, but
			// we should eventually figure out how to remove this
			// effectively
			if (comp.isDisposed())
				continue;

			// Invoke the event method on the component
			invokeMethod(comp, compMethod.method);
		}
	}

	/**
	 * Invokes the specified method on the component
	 * 
	 * @param component
	 *            the component on which the specified method should be called
	 * @param method
	 *            the method to be called
	 */
	protected void invokeMethod(Component component, Method method) {
		try {
			method.invoke(component);
		} catch (Exception e) {
			Debug.error(String.format("There was an issue trying to invoke method: %s on component: %s",
					method.getName(), component.getName()));
			e.printStackTrace();
		}
	}

	/**
	 * Clears the subscribed components
	 */
	public void dispose() {
		for (LinkedList<ComponentMethod> comps : _subscribedComponents.values())
			comps.clear();

		_subscribedComponents.clear();
	}

	/**
	 * The possible types of events a component can listen to
	 * 
	 * @author Brandon Porter
	 *
	 */
	public static enum ExecutionEvent {
		INITIALIZE(1, "init"), START(2, "start"), UPDATE(4, "update");

		private final int _bitValue;
		private final String _methodName;

		private ExecutionEvent(int bitValue, String methodName) {
			this._bitValue = bitValue;
			this._methodName = methodName;
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
	}

	/**
	 * Wrapper around a component and the method to be called during event
	 * dispatching
	 * 
	 * @author Brandon Porter
	 *
	 */
	private static class ComponentMethod {
		public final Component component;
		public final Method method;

		/**
		 * Constructs a component method
		 * 
		 * @param comp
		 *            the component
		 * @param meth
		 *            the method to be called
		 */
		public ComponentMethod(Component comp, Method meth) {
			this.component = comp;
			this.method = meth;
		}
	}
}
