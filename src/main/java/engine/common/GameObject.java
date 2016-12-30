package engine.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Brandon Porter
 *
 */
public class GameObject {
	private String _name = "GameObject";
	private List<Component> _components = new ArrayList<>();
	
	/**
	 * Constructs a new game object
	 */
	public GameObject() {
	}

	/**
	 * @return name of the game object
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Sets the name of the game object
	 * 
	 * @param name
	 *            name to be referenced by within a lookup table
	 */
	public void setName(String name) {
		this._name = name;
	}
}
