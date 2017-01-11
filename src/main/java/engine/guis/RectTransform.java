package engine.guis;

import engine.common.Transform;

/**
 * Inherits from the Transform component with the additional properties for 2D
 * transformations. This will only be useful to a GUI component.
 * 
 * @author Brandon Porter
 *
 */
public class RectTransform extends Transform {

	private float _width = 0;
	private float _height = 0;

	/**
	 * Constructs a new rect transform
	 */
	public RectTransform() {
	}

	/**
	 * @return the current width in pixels of the GUI game object
	 */
	public float getWidth() {
		return _width;
	}

	/**
	 * Sets the pixel width of the GUI game object
	 * 
	 * @param width
	 *            width in pixels
	 */
	public void setWidth(float width) {
		this._width = width;
	}

	/**
	 * @return the current height in pixels of the GUI game object
	 */
	public float getHeight() {
		return _height;
	}

	/**
	 * Sets the pixel height of the GUI game object
	 * 
	 * @param height
	 *            height in pixels
	 */
	public void setHeight(float height) {
		this._height = height;
	}
}
