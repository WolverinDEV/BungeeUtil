package dev.wolveringer.bungeeutil.profile;

public class Property {
	private final String name;
	private final String value;
	private final String signature;

	public Property(String value, String name) {
		this(value, name, null);
	}

	public Property(String name, String value, String signature) {
		this.name = name;
		this.value = value;
		this.signature = signature;
	}

	public String getName() {
		return this.name;
	}

	public String getSignature() {
		return this.signature;
	}

	public String getValue() {
		return this.value;
	}

	public boolean hasSignature() {
		return this.signature != null;
	}

	@Override
	public String toString() {
		return "Property@" + System.identityHashCode(this) + "[name=" + this.name + ", value=" + this.value + ", signature=" + this.signature + "]";
	}
}
