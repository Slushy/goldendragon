package engine.scenes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import engine.common.Component;
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
		for (ComponentMethod compMethod : _subscribedComponents.get(event)) {
			Component comp = compMethod.component;
			if (comp.isDisposed())
				continue;

			try {
				compMethod.method.invoke(comp);
				// if (event == ExecutionEvent.INITIALIZE) {
				// comp.init();
				// } else if (event == ExecutionEvent.RENDER) {
				// comp.render((SceneRenderer) args[0]);
				// } else if (event == ExecutionEvent.UPDATE) {
				// comp.update((InputHandler) args[0]);
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Clears the subscribed components
	 */
	public void dispose() {
		for(LinkedList<ComponentMethod> comps : _subscribedComponents.values())
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
		INITIALIZE(1, "init"), ON_FOREGROUND(2, "onForeground"), UPDATE(4, "update"), RENDER(8,
				"render"), ON_BACKGROUND(16, "onBackground");

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
