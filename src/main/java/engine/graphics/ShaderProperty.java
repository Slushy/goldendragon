package engine.graphics;

public enum ShaderProperty {
	COLOR("color");

	private final String _propertyName;

	private ShaderProperty(String propertyName) {
		this._propertyName = propertyName;
	}
}
