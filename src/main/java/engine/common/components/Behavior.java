package engine.common.components;

import engine.common.Component;

/**
 * A behavior is a custom script that is run in a scene
 * 
 * @author Brandon Porter
 *
 */
public abstract class Behavior extends Component {

	/**
	 * Constructs a new behavior
	 * 
	 * @param behaviourName
	 */
	public Behavior(String behaviourName) {
		super(behaviourName);
	}

}
